# JoinProjectTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 1
**Scope detail:** the rule's default non-outer config: an inner join whose two inputs are projects, each per-side projection a single uninterpreted expression, with the join condition an uninterpreted predicate over the projected row


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinProjectTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The before/after plans are structurally different (Project below the Join in before, Project above it in after), with correct symbol sharing — the same uninterpreted TL, TR and C appear on both sides, and the after-side condition is properly expanded over the raw join columns via correct joinField references (0,1 = L; 2,3 = R) — so the proof is non-vacuous and mirrors exactly what Calcite computes for an inner join (bottom program = projected expressions, top program = identity + condition, merged/expanded), with no missing preconditions for that case. The PARTIAL scope line honestly and specifically states the assumed restrictions (inner join, both inputs projects with a single uninterpreted expression each, condition over the projected row), so the result is a genuine, non-degenerate special case of the source rule: bag(σ_C(π(TL,L) × π(TR,R))) = π(TL,TR)(σ_{C∘(TL,TR)}(L × R)) for all instantiations of TL, TR, C. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8109250
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36038167
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 912000
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 565667
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21410292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36194709
  },
  "total_duration": {
    "secs": 0,
    "nanos": 73464209
  }
}
```
