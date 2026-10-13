# EliminateJoinUnderGroupByLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** the join is a LEFT join of two one-column scans, and the grouping operator is a group-by with exactly one group key referencing the left (preserved) input's column and no aggregate calls, so the rule's ProjectRemappedCols/PruneOrdering wrapper is the identity and the right input is simply dropped.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateJoinUnderGroupByLeft removes a Join operator and its right input if
it can be proven that the removal does not affect the output of the parent
grouping operator. This is the case if:

1. Only columns from the left input are being used by the grouping operator.

2. It can be proven that removal of the Join does not affect the result of the
grouping operator's aggregate functions.

3. The OrderingChoice of the grouping operator can be expressed with only
columns from the left input. Or in other words, at least one column in
every ordering group is one of the left output columns.

Condition #2 is only true when the following are all true:

1. All left rows are included in the output of the join. See the comment above
filtersMatchAllLeftRows in multiplicity_builder.go for more information on
when this is the case.
2. Either the join does not duplicate any left rows, or the join duplicates
left rows but the grouping operator's aggregate functions ignore duplicate
values. See the comment above filtersMatchLeftRowsAtMostOnce in
multiplicity_builder.go for more information on when rows are duplicated.
3. The join does not null-extend the left columns.

EliminateJoinUnderGroupByLeft should stay at the top of the file so that it
has a chance to fire before rules like EliminateDistinctOn that might prevent
matching.

Similar to the join-elimination rules that match on Project operators,
EliminateJoinUnderGroupByLeft can remap references to the right input of the
join to refer to equivalent columns on the left input.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderGroupByLeft`, not the other rules in that file):

```
# EliminateJoinUnderGroupByLeft removes a Join operator and its right input if
# it can be proven that the removal does not affect the output of the parent
# grouping operator. This is the case if:
#
# 1. Only columns from the left input are being used by the grouping operator.
#
# 2. It can be proven that removal of the Join does not affect the result of the
#    grouping operator's aggregate functions.
#
# 3. The OrderingChoice of the grouping operator can be expressed with only
#    columns from the left input. Or in other words, at least one column in
#    every ordering group is one of the left output columns.
#
# Condition #2 is only true when the following are all true:
#
# 1. All left rows are included in the output of the join. See the comment above
#    filtersMatchAllLeftRows in multiplicity_builder.go for more information on
#    when this is the case.
# 2. Either the join does not duplicate any left rows, or the join duplicates
#    left rows but the grouping operator's aggregate functions ignore duplicate
#    values. See the comment above filtersMatchLeftRowsAtMostOnce in
#    multiplicity_builder.go for more information on when rows are duplicated.
# 3. The join does not null-extend the left columns.
#
# EliminateJoinUnderGroupByLeft should stay at the top of the file so that it
# has a chance to fire before rules like EliminateDistinctOn that might prevent
# matching.
#
# Similar to the join-elimination rules that match on Project operators,
# EliminateJoinUnderGroupByLeft can remap references to the right input of the
# join to refer to equivalent columns on the left input.
[EliminateJoinUnderGroupByLeft, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    $input:(InnerJoin | LeftJoin $left:*)
    $aggs:*
    $private:(GroupingPrivate $groupingCols:* $ordering:*) &
        (OrderingCanProjectCols
            $ordering
            $leftCols:(OutputCols $left)
        ) &
        (CanRemapCols
            $toRemap:(UnionCols
                $groupingCols
                (AggregationOuterCols $aggs)
            )
            $leftCols
            $fds:(FuncDeps $input)
        ) &
        (CanUseImprovedJoinElimination $toRemap $leftCols) &
        (CanEliminateJoinUnderGroupByLeft $input $aggs)
)
=>
((OpName)
    (Project
        $left
        (ProjectRemappedCols $toRemap $leftCols $fds)
        $leftCols
    )
    $aggs
    (MakeGrouping
        $groupingCols
        (PruneOrdering $ordering $leftCols)
    )
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is genuinely non-vacuous (before() contains the LEFT join plus the right scan; after() drops both) and the symbol sharing is correct — the group key join.field(0) is the same preserved left column as left.field(0), and the join condition stays uninterpreted, so QED's proof holds for *any* join predicate, which is exactly the special case the rule covers. None of the source rule's side conditions are silently missing: a LEFT join includes every left row and never null-extends left columns, the absence of aggregate calls makes the "aggs must ignore duplicates" condition moot, there is no ordering to constrain, and the group key references the left column directly so ProjectRemappedCols/PruneOrdering are identity — the remaining generality (InnerJoin, multi-column remapping via functional dependencies, duplicate-tolerant aggregates, ordering) is unreachable by QED itself, not by a missing DSL feature. The PARTIAL scope line is accurate, specific, and the fragment proved (distinct of a left column under a LEFT join equals distinct of that column over the left input alone) is a real, useful piece of the original rule. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12327706
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35240583
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 891791
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 670375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 33507750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35391375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 85829375
  }
}
```
