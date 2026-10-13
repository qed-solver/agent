# EliminateAggDistinctForKeys

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** the aggregation argument is the input scan's unique key, which is also the (single-column) grouping key, so each group holds at most one row and AggDistinct is a per-group no-op; the general CanRemoveAggDistinctForKeys condition (grouping cols functionally determine the agg arg over any input) is not expressible because QED only models scan uniqueness as a functional dependency.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateAggDistinctForKeys eliminates unnecessary AggDistinct modifiers when
it is known that the aggregation argument is unique within each group.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateAggDistinctForKeys`, not the other rules in that file):

```
# EliminateAggDistinctForKeys eliminates unnecessary AggDistinct modifiers when
# it is known that the aggregation argument is unique within each group.
[EliminateAggDistinctForKeys, Normalize]
(GroupBy | ScalarGroupBy
    $input:* & (HasStrictKey $input)
    $aggregations:[
        ...
        $item:(AggregationsItem (AggDistinct $agg:*))
        ...
    ]
    $groupingPrivate:* &
        (CanRemoveAggDistinctForKeys
            $input
            $groupingPrivate
            $agg
        )
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem $aggregations $item $agg)
    $groupingPrivate
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures a genuine special case of the rule (input is a scan whose unique key is also the grouping key and the DISTINCT aggregate's argument), `before()` and `after()` differ only in the `distinct` flag on an otherwise-identical uninterpreted aggregate call so the proof is not vacuous, the `unique=true` scan constraint is precisely the precondition QED needs to equate the per-group bag with the per-group set (making the SMT step non-trivial), and the PARTIAL scope tag honestly and specifically documents that the general "grouping cols functionally determine the agg arg over an arbitrary input" condition is inexpressible — leaving a useful, non-degenerate result.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8387917
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 38387958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 908750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 555000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21002250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 38553458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 75363042
  }
}
```
