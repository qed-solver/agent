# ReduceNotNullGroupingCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3
**Scope detail:** the non-null redundant grouping column is assumed to be literally the same field as the surviving grouping column (a 2-column input whose two grouping columns are equal), and the UpsertDistinctOn has no aggregation output columns.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

ReduceNotNullGroupingCols is similar to ReduceGroupingCols, but with the
additional restriction that nullable columns cannot be removed from the set of
grouping columns. This is because the UpsertDistinctOn operator treats NULL
values as not equal to one another, and therefore will not group them
together. Since removing a grouping column is equivalent to grouping all
values of that column together, this would be incorrect in the case where all
input rows are NULL for that column:

SELECT c FROM t WHERE c IS NULL

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `ReduceNotNullGroupingCols`, not the other rules in that file):

```
# ReduceNotNullGroupingCols is similar to ReduceGroupingCols, but with the
# additional restriction that nullable columns cannot be removed from the set of
# grouping columns. This is because the UpsertDistinctOn operator treats NULL
# values as not equal to one another, and therefore will not group them
# together. Since removing a grouping column is equivalent to grouping all
# values of that column together, this would be incorrect in the case where all
# input rows are NULL for that column:
#
#   SELECT c FROM t WHERE c IS NULL
#
[ReduceNotNullGroupingCols, Normalize]
(UpsertDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        ^(ColsAreEmpty
            $redundantCols:(IntersectionCols
                (RedundantCols
                    $input
                    (GroupingCols $groupingPrivate)
                )
                (NotNullCols $input)
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

**Verdict:** CONFIRMED

The encoding is a correct, non-degenerate special case: `before()` (GROUP BY col0, col1 where col0≡col1) and `after()` (GROUP BY col0, then project col0 twice) are structurally distinct and the proof is non-vacuous. The scope limitation—making the "redundant" column literally the same field rather than a distinct-but-functionally-dependent column—is forced by the DSL's inability to express functional dependencies between separate columns, and QED's inability to reason about ConstAgg's algebra on a distinct dependent column, making the fully general rule unprovable without (and likely even with) DSL extensions. The SCOPE line accurately and specifically states both assumptions (literal field identity and no aggregation columns), the non-nullable type faithfully captures the source rule's `NotNullCols` precondition, and the overall shape (reducing the grouping set while preserving output width via a pass-through) mirrors the source rule's rewrite.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7108125
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33473167
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 892667
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 545167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19112500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33548792
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68223750
  }
}
```
