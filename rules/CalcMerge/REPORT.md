# CalcMerge

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 14  **Verification rounds used:** 2
**Scope detail:** fixed shape: 2-column input; each Calc has one uninterpreted filter predicate over both input columns and exactly two uninterpreted projection expressions, each over both columns


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CalcMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures CalcMerge's core transformation — the top Calc's condition and projections substituted over the bottom Calc's projection, with the two filters conjoined — and `before()` (filter→project→filter→project) genuinely differs from `after()` (single conjoined filter→project), so the proof is non-vacuous; bottom and top use independent symbols and the merged side correctly reuses the top's symbols while retaining the bottom's condition, so the substitution proven is the real one. The only narrowing is a fixed 2-column / 2-projection shape (the "over both columns" choice is a superset, not an under-generalization, since any subset-referencing expression is also a 2-arg function), which a single concrete-arity pattern fundamentally cannot generalize and which is accurately tagged `SCOPE: PARTIAL`. No triviality, wrong operator shape, harmful symbol-reuse, or silently-dropped precondition: the rule's no-windowed-aggregate guard is inherent in modeling the top's expressions as per-row uninterpreted functions, which window aggregates cannot be.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5470458
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 31577666
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 854166
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 446542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16501083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 31669833
  },
  "total_duration": {
    "secs": 0,
    "nanos": 63965583
  }
}
```
