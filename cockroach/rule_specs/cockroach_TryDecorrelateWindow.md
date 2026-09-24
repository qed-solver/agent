# Name: TryDecorrelateWindow
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateWindow "pushes down" a Join into a Window operator, in an
attempt to keep "digging" down to find and eliminate unnecessary correlation.
The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
operator into a non-apply Join operator. This rule is very similar to
TryDecorrelateGroupBy.

This rule adds the output columns of the left side of the join to the Window
operator's partition cols. This effectively means that each row of the left
side of the join is windowed independently, assuming the left side has a key
(and if it doesn't, we can give it one via EnsureKey).

SELECT
left.k, left.x, right.x, rank
FROM
left
INNER JOIN LATERAL (
SELECT rank() OVER () AS rank, right.x FROM (SELECT * FROM right WHERE left.k = right.k)
)
=>
SELECT
left.k, left.x, right.x, rank() OVER (PARTITION BY left.k) AS rank
FROM
left INNER JOIN right ON left.k = right.k

Sketch of why this rule works (assume A has a key):

Recall from [3] that the definition of Apply (for cross joins) is:

(InnerJoinApply A E true) = (Union_{r ∈ A} {r} × E(r))

Where E is a relational expression mapping rows r ∈ A to relational result
sets.

Starting with (InnerJoinApply A (Window B partcols) on), where P is the set of
partition columns and p is the join predicate.

= (Select (InnerJoinApply A (Window B partcols) true) on)

By the inverse of MergeSelectInnerJoin.

= (Select
(Union_{r ∈ A} {r} × (Window B partcols)(r))
on
)

By the definition of Apply.

= (Select
(Union_{r ∈ A} {r} × (Window B(r) partcols))
on
)

By the fact that by construction, window functions only refer to
variable references in their input.

= (Select
(Union_{r ∈ A} (Window {r} × B(r) partcols))
on
)

Because the Window only looks at columns from B(r).

= (Select
(Window
(Union_{r ∈ A} {r} × B(r))
(Union partcols (KeyCols A))
)
on
)

Roughly, since A has a key, partitioning (Union_{r ∈ A} r × B(r)) by the key
of A results in exactly one partition for each row in A, and so partitioning
higher up has the same effect as performing the window function for each row.

= (Select
(Window
(InnerJoinApply A B true)
(Union partcols (OutputCols A))
)
on
)

Again by the definition of Apply.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateWindow`, not the other rules in that file):

```
# TryDecorrelateWindow "pushes down" a Join into a Window operator, in an
# attempt to keep "digging" down to find and eliminate unnecessary correlation.
# The eventual hope is to trigger the DecorrelateJoin rule to turn a JoinApply
# operator into a non-apply Join operator. This rule is very similar to
# TryDecorrelateGroupBy.
#
# This rule adds the output columns of the left side of the join to the Window
# operator's partition cols. This effectively means that each row of the left
# side of the join is windowed independently, assuming the left side has a key
# (and if it doesn't, we can give it one via EnsureKey).
#
# SELECT
#     left.k, left.x, right.x, rank
# FROM
#   left
#   INNER JOIN LATERAL (
#     SELECT rank() OVER () AS rank, right.x FROM (SELECT * FROM right WHERE left.k = right.k)
#   )
# =>
# SELECT
#   left.k, left.x, right.x, rank() OVER (PARTITION BY left.k) AS rank
# FROM
#   left INNER JOIN right ON left.k = right.k
#
# Sketch of why this rule works (assume A has a key):
#
# Recall from [3] that the definition of Apply (for cross joins) is:
#
#  (InnerJoinApply A E true) = (Union_{r ∈ A} {r} × E(r))
#
# Where E is a relational expression mapping rows r ∈ A to relational result
# sets.
#
# Starting with (InnerJoinApply A (Window B partcols) on), where P is the set of
# partition columns and p is the join predicate.
#
#  = (Select (InnerJoinApply A (Window B partcols) true) on)
#
# By the inverse of MergeSelectInnerJoin.
#
#  = (Select
#      (Union_{r ∈ A} {r} × (Window B partcols)(r))
#      on
#    )
#
# By the definition of Apply.
#
#  = (Select
#      (Union_{r ∈ A} {r} × (Window B(r) partcols))
#      on
#    )
#
# By the fact that by construction, window functions only refer to
# variable references in their input.
#
#  = (Select
#      (Union_{r ∈ A} (Window {r} × B(r) partcols))
#      on
#    )
#
# Because the Window only looks at columns from B(r).
#
#  = (Select
#      (Window
#        (Union_{r ∈ A} {r} × B(r))
#        (Union partcols (KeyCols A))
#      )
#      on
#    )
#
# Roughly, since A has a key, partitioning (Union_{r ∈ A} r × B(r)) by the key
# of A results in exactly one partition for each row in A, and so partitioning
# higher up has the same effect as performing the window function for each row.
#
#  = (Select
#      (Window
#        (InnerJoinApply A B true)
#        (Union partcols (OutputCols A))
#      )
#      on
#    )
#
# Again by the definition of Apply.
[TryDecorrelateWindow, Normalize]
(InnerJoinApply | InnerJoin
    $left:*
    $right:(Window $input:* $windows:* $private:*) &
        (HasOuterCols $right)
    $on:*
    $joinPrivate:*
)
=>
(Project
    # Needed to project away any columns added by EnsureKey.
    (Select
        (Window
            ((OpName)
                $newLeft:(EnsureKey $left)
                $input
                []
                $joinPrivate
            )
            $windows
            (AddColsToPartition $private (KeyCols $newLeft))
        )
        $on
    )
    []
    (OutputCols2 $left $right)
)
```
