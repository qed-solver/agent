# FilterWindowTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 15  **Verification rounds used:** 1
**Scope detail:** the window is modeled as a group-by/aggregate with a single


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterWindowTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, honest special case: since QED cannot model Window at all (no bag semantics, no JSON/prover support), modeling it as a group-by over the partition key with an uninterpreted aggregate captures exactly the rule's core mechanism, and the proof is non-trivial (Filter(P(k)∧Q(k,f), GroupBy_k f(v)) vs Filter(Q, GroupBy_k f(v) over Filter(P(k), B)) genuinely differ and are equivalent only because P, an uninterpreted predicate structurally referencing the partition key alone — mirroring Calcite's `partitionKeys.contains(usedCols)` check — is constant per partition while Q, which references the aggregate value, correctly stays above). Symbol sharing is correct (same scan B, P, Q, f on both sides, with P's key column resolving consistently to the group key output column), no preconditions are silently dropped, and the multi-line but specific PARTIAL scope tag accurately states the real restrictions (single partition key, single aggregate, one pushed + one remaining conjunct, per-partition rather than per-row window output).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11060336
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 55846125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 971875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 797708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26518000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 56083125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 98738958
  }
}
```
