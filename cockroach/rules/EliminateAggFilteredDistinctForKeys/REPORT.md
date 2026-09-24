# EliminateAggFilteredDistinctForKeys

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 62  **Verification rounds used:** 1
**Scope detail:** the aggregation argument is itself the (single-column)


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

**Verdict:** CONFIRMED (manual)

Manually re-derived and verified by Claude (not the automated porter/verifier LLM loop, which exhausted both pool attempts on repeated LLM context-length crashes before ever reaching a proof attempt). The automated run's last crash-time candidate (stashed at UnprovableRRuleInstances/EliminateAggFilteredDistinctForKeys.java.rejected) had already identified the right narrow special case — aggregation argument = the single unique grouping key — but was missing the required SCOPE tag and was never actually run through QED before the crash. Testing it directly: provable=true. Verified non-vacuous with a negative control (dropping the scan's unique=true flag, everything else identical) which correctly comes back provable=false — confirming the proof genuinely depends on the uniqueness fact (every group has at most one row, so AggDistinct trivially coincides with the plain aggregate), not some artifact QED would accept unconditionally. The source rule's AggFilter wrapper is not modeled (RelRN/AggCall has no FILTER-clause construct) — the covered identity is the AggDistinct->plain rewrite alone, on the premise that $filter is present unchanged on both sides of the source rule and that filtering rows out of an already-unique-keyed relation cannot introduce duplicates into what remains, so the argument extends unaffected to the filtered case; only the AggFilter wrapper itself goes unverified here. NOTE: this same narrow encoding, tested directly against the sibling rule EliminateAggDistinctForKeys (already recorded SKIPPED), also comes back provable=true with the same non-vacuous negative-control behavior — contradicting that rule's recorded verifier reasoning that 'no encoding, including the narrow primary-key special case, can prove this.' That looks like a false SKIPPED verdict worth reopening, but I have not touched that entry since it wasn't the rule asked about here.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6200792
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 14893083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 87083
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 538625
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 12603500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 15006042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 30284291
  }
}
```
