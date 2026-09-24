# AggregateJoinTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 126  **Verification rounds used:** 5
**Scope detail:** DEFAULT config only (no aggregate-function calls); INNER join; group set spans both sides (join key + one extra column per side). EXTENDED config with splittable aggregate functions is unsupported (uninterpreted agg semantics).


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateJoinTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED (hand-applied fix)

The original porter attempts FAILED after exhausting all 5 rounds to context-length crashes, never reaching a conclusion. The prior round's independent verifier reviewed the FAILED outcome anyway and returned DISAGREE, pointing out that QedTable already supports multi-column tables and proposing a concrete, achievable PARTIAL-scope encoding: Agg(x,y; no calls; Join(L(a,x), R(b,y), P(a,b))) => Join(Agg(a,x;L), Agg(b,y;R), P(a',b')), re-grouped by (x,y) above the rejoined result -- exactly Calcite's DEFAULT (no aggregate-function) config of this rule. The harness operator hand-applied this: added an additive RelRN.scanMany/ScanMany multi-column scan (QED already supported multi-column tables via QedTable; RelRN just never exposed the constructor), encoded the rule exactly as the verifier described, and QED proved it (provable=true). The extension passed a fresh compile, a fresh re-proof of all 27 previously-confirmed-PROVED rules (0 regressions), and an independent LLM auditor review (VERDICT: SAFE -- purely additive, follows the existing ProjectMany precedent, and is a genuine necessity for this fragment). The EXTENDED config (splittable aggregate functions pushed through the join) remains out of scope: QED treats every aggregate call as an uninterpreted function of its input bag, so it cannot verify function-specific splitting identities (e.g. SUM(x) = sum of per-side partial sums).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9302166
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6322833
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 86083
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 713583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18541666
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6382167
  },
  "total_duration": {
    "secs": 0,
    "nanos": 27863916
  }
}
```
