# PushLimitIntoWindow

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/window.opt

PushLimitIntoWindow moves a Limit below a Window when able. This is
all-or-nothing. Even if we could push the limit below *some* of the window
functions, if there are any we cannot, then we don't. This is because
computing additional window functions is not that expensive, and the
expensive part is doing the sorting and partitioning. Once exec supports
passing orderings through and does not require re-partitioning and re-sorting
of window functions, pushing past some-but-not-all of the window functions
might be profitable.

SELECT rank() OVER (ORDER BY c) FROM abc ORDER BY c LIMIT 10
=>
SELECT
rank() OVER (ORDER BY c)
FROM
(SELECT c FROM abc ORDER BY c LIMIT 10)

SELECT rank() OVER (PARTITION BY b ORDER BY c) FROM abc LIMIT 10
=>
SELECT
rank() OVER (PARTITION BY b ORDER BY c)
FROM
(SELECT b, c FROM abc ORDER BY b, c LIMIT 10)

First, we construct a "segmented ordering" consisting of the Window's
partition columns followed by its ordering columns (the relative positions of
the partition columns are arbitrary). This ordering is useful because it
performs the partitioning and then the ordering within each partition.  If
this ordering does not imply the Limit's ordering, we do not proceed.

Since we now know that the segmented ordering is stronger than the Limit's
ordering, it's safe to replace the limit's ordering with it.

The Limit having the segmented ordering means that there are three kinds of
partitions:
1. those that are completely contained within the limited set of rows,
2. those that are completely excluded from the set of rows, and
3. *at most one* partition which is "cut off" partway through.
Including the window function's ordering in the Limit's ordering does not
matter for (1)- and (2)-style partitions (since the window function itself
will re-sort them), but for the (3)-style partition, we need to ensure that
the limit operator allows through a prefix of it, rather than an arbitrary
subset.

Finally, we require that every window function+frame pair being computed has
the "prefix-safe" property. A window function is prefix safe if it can be
correctly computed over only a prefix of a partition. For example, rank() has
this property because rows that come later in the ordering don't affect the
rank of the rows before, but avg()+UNBOUNDED {PRECEDING,FOLLOWING} doesn't,
because we must see the entire partition to compute the average over it.

TODO(justin): Add a rule that translates a limit with an ordering on rank()
or dense_rank() into one using the ordering of the window function. This will
allow us to push down limits in cases like:

SELECT rank() OVER (ORDER BY f) rnk FROM a ORDER BY rnk LIMIT 10
=>
SELECT rank() OVER (ORDER BY f) rnk FROM a ORDER BY f LIMIT 10
=>
SELECT rank() OVER (ORDER BY f) rnk FROM (SELECT * FROM a ORDER BY f LIMIT 10)

Extracted from `window.opt` (which defines multiple rules — implement specifically `PushLimitIntoWindow`, not the other rules in that file):

```
# PushLimitIntoWindow moves a Limit below a Window when able. This is
# all-or-nothing. Even if we could push the limit below *some* of the window
# functions, if there are any we cannot, then we don't. This is because
# computing additional window functions is not that expensive, and the
# expensive part is doing the sorting and partitioning. Once exec supports
# passing orderings through and does not require re-partitioning and re-sorting
# of window functions, pushing past some-but-not-all of the window functions
# might be profitable.
# 
# SELECT rank() OVER (ORDER BY c) FROM abc ORDER BY c LIMIT 10
# => 
# SELECT
#     rank() OVER (ORDER BY c)
# FROM
#     (SELECT c FROM abc ORDER BY c LIMIT 10)
# 
# SELECT rank() OVER (PARTITION BY b ORDER BY c) FROM abc LIMIT 10
# => 
# SELECT
#     rank() OVER (PARTITION BY b ORDER BY c)
# FROM
#     (SELECT b, c FROM abc ORDER BY b, c LIMIT 10)
# 
# First, we construct a "segmented ordering" consisting of the Window's
# partition columns followed by its ordering columns (the relative positions of
# the partition columns are arbitrary). This ordering is useful because it
# performs the partitioning and then the ordering within each partition.  If
# this ordering does not imply the Limit's ordering, we do not proceed.
# 
# Since we now know that the segmented ordering is stronger than the Limit's
# ordering, it's safe to replace the limit's ordering with it.
# 
# The Limit having the segmented ordering means that there are three kinds of
# partitions:
#   1. those that are completely contained within the limited set of rows,
#   2. those that are completely excluded from the set of rows, and
#   3. *at most one* partition which is "cut off" partway through.
# Including the window function's ordering in the Limit's ordering does not
# matter for (1)- and (2)-style partitions (since the window function itself
# will re-sort them), but for the (3)-style partition, we need to ensure that
# the limit operator allows through a prefix of it, rather than an arbitrary
# subset.
# 
# Finally, we require that every window function+frame pair being computed has
# the "prefix-safe" property. A window function is prefix safe if it can be
# correctly computed over only a prefix of a partition. For example, rank() has
# this property because rows that come later in the ordering don't affect the
# rank of the rows before, but avg()+UNBOUNDED {PRECEDING,FOLLOWING} doesn't,
# because we must see the entire partition to compute the average over it.
#
# TODO(justin): Add a rule that translates a limit with an ordering on rank()
# or dense_rank() into one using the ordering of the window function. This will
# allow us to push down limits in cases like:
#
# SELECT rank() OVER (ORDER BY f) rnk FROM a ORDER BY rnk LIMIT 10
# =>
# SELECT rank() OVER (ORDER BY f) rnk FROM a ORDER BY f LIMIT 10
# =>
# SELECT rank() OVER (ORDER BY f) rnk FROM (SELECT * FROM a ORDER BY f LIMIT 10)
[PushLimitIntoWindow, Normalize]
(Limit
    (Window $input:* $fns:* & (AllArePrefixSafe $fns) $private:*)
    $limit:*
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $inputCols:(OutputCols $input)
        ) &
        (Let
            ($newOrdering $ok):(MakeSegmentedOrdering
                $input
                (WindowPartition $private)
                (WindowOrdering $private)
                $ordering
            )
            $ok
        )
)
=>
(Window
    (Limit
        $input
        $limit
        (PruneOrdering
            (DerefOrderingChoice $newOrdering)
            $inputCols
        )
    )
    $fns
    $private
)
```
```

## Independent verifier review

**Verdict:** AGREE

PushLimitIntoWindow's soundness rests entirely on two things QED's model categorically lacks: the row-ordering semantics of Limit/Sort (taking the first N rows under the window's segmented partition+ordering, so the one "cut off" partition must pass through as a *prefix*), and the prefix-safety property of specific window functions (rank() yes, avg() with an unbounded frame no) — a bespoke internal semantics of the operator that QED cannot see through as an uninterpreted function. QED only reasons about bag-equality of operator inputs, so it cannot know that W∘Limit = Limit∘W holds for a "prefix-safe" W; even if the DSL were extended to build Sort/Limit/Window shapes (the JSON serializer already carries LogicalSort), the prover would have to derive an identity between two distinct nestings of uninterpreted ordering-dependent operators, which is not a bag-algebra theorem and would simply fail. This is a genuine fundamental limitation of the model, not a missing encoding trick, so the porter's UNSUPPORTED conclusion is correct. ```
