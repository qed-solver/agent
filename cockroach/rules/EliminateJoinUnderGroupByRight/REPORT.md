# EliminateJoinUnderGroupByRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 63  **Verification rounds used:** 4
**Scope detail:** the join is an INNER self-join on equality over a single non-nullable key column, and the grouping operator is a group-by with exactly one group key referencing the right (kept) input's column and no aggregate calls, so the left input is simply dropped.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateJoinUnderGroupByRight is symmetric with
EliminateJoinUnderGroupByLeft, except that it matches on InnerJoins.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderGroupByRight`, not the other rules in that file):

```
# EliminateJoinUnderGroupByRight is symmetric with
# EliminateJoinUnderGroupByLeft, except that it matches on InnerJoins.
[EliminateJoinUnderGroupByRight, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    $input:(InnerJoin * $right:*)
    $aggs:*
    $private:(GroupingPrivate $groupingCols:* $ordering:*) &
        (OrderingCanProjectCols
            $ordering
            $rightCols:(OutputCols $right)
        ) &
        (CanRemapCols
            $toRemap:(UnionCols
                $groupingCols
                (AggregationOuterCols $aggs)
            )
            $rightCols
            $fds:(FuncDeps $input)
        ) &
        (CanUseImprovedJoinElimination $toRemap $rightCols) &
        (CanEliminateJoinUnderGroupByRight $input $aggs)
)
=>
((OpName)
    (Project
        $right
        (ProjectRemappedCols $toRemap $rightCols $fds)
        $rightCols
    )
    $aggs
    (MakeGrouping
        $groupingCols
        (PruneOrdering $ordering $rightCols)
    )
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a non-vacuous, sound special case whose PARTIAL scope is honestly and specifically declared: before() genuinely contains the equality self-join that after() eliminates, and the equivalence holds universally because every non-null right row self-matches under the equality condition while the aggregate-free group-by collapses the duplication the join introduces. The concrete EQUALS condition, the non-nullable key column, and the shared scan (making it a self-join) are exactly the assumptions that make the source rule's unverifiable preconditions — all-right-rows-survive, functional-dependency-based column remapping, and ordering constraints — hold structurally, and all of them are named in the SCOPE line rather than hidden as symbol-sharing or silently dropped preconditions. This is a genuine, non-degenerate corner of the rule (dropping an O(n²) self-join beneath a distinct-on) that QED fundamentally cannot certify for arbitrary independent join inputs and uninterpreted conditions, so the provable result is faithful to what it claims. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6563289
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6324833
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 58958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 439500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 13259708
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6362084
  },
  "total_duration": {
    "secs": 0,
    "nanos": 22228500
  }
}
```
