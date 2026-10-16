# EliminateAggFilteredDistinctForKeys

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 3
**Scope detail:** no grouping columns (ScalarGroupBy), where the per-call aggregate FILTER is encoded as a relational Filter below the aggregate (equivalent for the single group), and the aggregate argument is the scan's unique key.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateAggFilteredDistinctForKeys is similar to EliminateAggDistinctForKeys,
except that it works when an AggFilter operator is also present.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateAggFilteredDistinctForKeys`, not the other rules in that file):

```
# EliminateAggFilteredDistinctForKeys is similar to EliminateAggDistinctForKeys,
# except that it works when an AggFilter operator is also present.
[EliminateAggFilteredDistinctForKeys, Normalize]
(GroupBy | ScalarGroupBy
    $input:* & (HasStrictKey $input)
    $aggregations:[
        ...
        $item:(AggregationsItem
            (AggFilter (AggDistinct $agg:*) $filter:*)
        )
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
    (ReplaceAggregationsItem
        $aggregations
        $item
        (AggFilter $agg $filter)
    )
    $groupingPrivate
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() differ genuinely (distinct=true vs false on the same aggregate, a flag the JSONSerializer does pass to QED), and the proof is non-vacuous: the scan's key on the aggregate's argument forces the argument values to be unique, so dedup is the identity and the uninterpreted aggregate's input bags are equal. The encoding is a sound, honestly-tagged PARTIAL special case — ScalarGroupBy (where encoding AggFilter as a pre-aggregate Filter is strictly equivalent, as the SCOPE line notes) with the argument as the unique key, which satisfies the original rule's HasStrictKey + CanRemoveAggDistinctForKeys preconditions — with no hard-coded predicates, wrong operator shapes, or missing premises. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6724750
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 38066374
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1087791
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 479416
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 17922416
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 38206708
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71677500
  }
}
```
