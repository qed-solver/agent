# AggregateExpandWithinDistinct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** no grouping, and a single COUNT(*) WITHIN DISTINCT (x) call (which the rule documents as identical to COUNT(DISTINCT x)) rewritten to its dedup-then-count expansion


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExpandWithinDistinctRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous (a single COUNT(DISTINCT x) aggregate vs. a nested dedup-then-count plan), and tracing the source rule on the no-grouping, single `COUNT(*) WITHIN DISTINCT (x)` input (empty arg list, no filter, one grouping set ⇒ no GROUPING()/MIN/MAX/throw clauses are emitted) shows it produces exactly this dedup-then-count shape, with the input written as COUNT(DISTINCT x) — the documented-identical form that the DSL can express since AggCall has no distinctKeys/filter fields. The before/after pair also holds under real bag+NULL semantics (both sides count distinct non-null x: the outer count takes the dedup key as operand, so a NULL group is ignored just as in COUNT(DISTINCT x)), and the PARTIAL scope line honestly and specifically states the assumed restriction (no grouping, one call); full-rule generality (grouping sets, GROUPING(), filters, THROW_UNLESS) is genuinely inexpressible in the current DSL, so the special case is a legitimate, non-degenerate result.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9159501
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 44041042
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1130875
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 516209
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22314250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 44178583
  },
  "total_duration": {
    "secs": 0,
    "nanos": 82157166
  }
}
```
