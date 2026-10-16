# ReduceGroupingCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 28  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

ReduceGroupingCols eliminates redundant grouping columns from the GroupBy
operator and replaces them by ConstAgg aggregate functions. A grouping
column is redundant if it is functionally determined by the other grouping
columns. If that's true, then its value must be constant within a group.
Therefore, it has no effect on the grouping and can instead be represented as
an ConstAgg aggregate, since all rows in the group have the same value for
that column.

Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
find. If a test case for EnsureDistinctOn is found, it should be added to the
match pattern.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ReduceGroupingCols`, not the other rules in that file):

```
# ReduceGroupingCols eliminates redundant grouping columns from the GroupBy
# operator and replaces them by ConstAgg aggregate functions. A grouping
# column is redundant if it is functionally determined by the other grouping
# columns. If that's true, then its value must be constant within a group.
# Therefore, it has no effect on the grouping and can instead be represented as
# an ConstAgg aggregate, since all rows in the group have the same value for
# that column.
#
# Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
# find. If a test case for EnsureDistinctOn is found, it should be added to the
# match pattern.
[ReduceGroupingCols, Normalize]
(GroupBy | DistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        ^(ColsAreEmpty
            $redundantCols:(RedundantCols
                $input
                (GroupingCols $groupingPrivate)
            )
        )
)
=>
((OpName)
    $input
    (AppendAggCols $aggregations ConstAgg $redundantCols)
    (RemoveGroupingCols $groupingPrivate $redundantCols)
)
```
```

## Independent verifier review

**Verdict:** AGREE

ReduceGroupingCols is sound only because of a specific algebraic identity — ConstAgg/ANY_VALUE(r) over a group returns the group's (constant) value of the redundant column r — and QED models every aggregate as an uninterpreted bag-to-scalar operator with no axioms (it can only relate aggregate calls when their input bags are provably equal, qed.pdf §6.2). Here the rewrite produces an uninterpreted f(bag) on the after-side versus the raw column value on the before-side; no SMT decision procedure can equate those for uninterpreted f, and the FD/key precondition (even if fully expressible, which would require a DSL extension for multi-column key constraints) cannot supply the missing axiom, since that gap is inside the trusted prover, not the DSL. Consequently every instance of this rewrite, including any key-based special case, requires the same unprovable identity, so no narrower provable special case exists and the UNSUPPORTED verdict stands. ```
