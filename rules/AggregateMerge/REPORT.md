# AggregateMerge

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 37  **Verification rounds used:** 4
**Scope detail:** the top aggregate has no aggregate calls, the bottom aggregate has exactly one aggregate call (which the merged aggregate drops), the top's single group key is exactly the bottom's first group key, the bottom has exactly two group keys, and the input is a one-column scan.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-vacuous instance of the source rule: when the top aggregate has no aggregate calls, Calcite's onMatch skips the call-merging loop entirely and emits exactly the encoded after() — a single aggregate over the bottom's input, grouped by the top's (permuted) group keys, with the bottom's unreferenced calls dropped — so the proved equivalence AGG_{k1}(AGG_{k1,k2; v(x)}(S)) ≡ AGG_{k1}(S) is the real two-level-collapse transformation with correct symbol sharing (k1 reused between bottom's first group key and the merged key, which is load-bearing; v dropped because no top call references it). The PARTIAL tag honestly and specifically lists every assumption (no top calls, exactly one bottom call, 2→1 group keys, one-column scan input), and excluding the call-merging half (SUM-of-SUM, etc.) is forced by QED's fundamental inability to reason about aggregate-function algebra with uninterpreted symbols, not by a missing DSL capability, so this is the maximal provable core of the rule rather than an avoidable narrowing.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10809126
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35054125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 943125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 685542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26776875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35172584
  },
  "total_duration": {
    "secs": 0,
    "nanos": 82287750
  }
}
```
