# JoinPushTransitivePredicates

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 27  **Verification rounds used:** 2
**Scope detail:** INNER single-key equi-join, one uninterpreted right-input key predicate pushed to the equal left-input key only


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinPushTransitivePredicatesRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The rewrite is non-vacuous — `after()` genuinely adds `Filter(p, LeftInput)` on top of the same join — and the equivalence it proves (`x = y ∧ p(y) ⟹ p(x)`, so the added left filter is redundant) is exactly the transitive-predicate inference `JoinPushTransitivePredicatesRule` performs via `RelMdPredicates` and applies by adding a filter to the left input while keeping the join condition unchanged, with the shared `p`/`EQUALS`/`joinCond` symbols being the *correct* identifications rather than over-constraint (the same predicate on both keys is the essence of "transitive"; two independent symbols would make the rewrite invalid). The restrictions — INNER join, single-key equality, non-nullable key type, one-sided push — are a genuine special case forced by QED's documented inability to infer entailment between uninterpreted predicates from an arbitrary join condition (a real prover limitation, not a missing DSL operator), the `// SCOPE: PARTIAL` line honestly names them, and no implicit primary-key/uniqueness guarantee is leaned on (both scans are marked non-unique), so the proof is of the same claim the rule makes in this instance.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8327166
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36408458
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 809708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 658042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21539875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36540500
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74281250
  }
}
```
