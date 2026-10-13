# AggregateUnionTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 25  **Verification rounds used:** 2
**Scope detail:** the aggregate is a group-by-all with no aggregate calls (dedup / DISTINCT) and the UNION ALL has exactly two inputs sharing the same two-column row type.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateUnionTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully represents the no-aggregate-call (DISTINCT / group-by-all) limit of `AggregateUnionTranspose` over UNION ALL, with structurally distinct before (dedup over union of A,B) and after (dedup over union of per-input dedups) that exactly mirrors what the real rule emits (empty `aggCallList`, empty `transformAggCalls`, top group-by-all), and this narrowing is genuinely forced by QED's fundamental inability to model aggregate-function algebra (COUNT→SUM0, MIN/MAX/SUM distribution) rather than a missing DSL builder; the two union inputs correctly share a single row type (a real UNION requirement, not an over-constraint), the `all=true` flag and dedup aggregate shape match the source, and the `// SCOPE: PARTIAL` line honestly and specifically states the no-calls / two-input / two-column assumptions, so the non-vacuous proof is neither trivial nor misleading.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 22546457
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33949083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 928250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 648416
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 48715500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34094250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 98375917
  }
}
```
