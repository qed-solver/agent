# ReduceGroupingCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3

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

Any nontrivial firing of ReduceGroupingCols must prove that the removed grouping column is equal to a new `ConstAgg` of that column, but QED treats aggregate calls as uninterpreted except for equality of their input bags and has no algebra for `ConstAgg` picking the group-constant value. The rule also depends on the `RedundantCols` functional-dependency guard, which is a cross-row property the DSL/QED cannot assume, so no non-vacuous instance of this rewrite is provable.
