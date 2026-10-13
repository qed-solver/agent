# JoinCommute

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 39  **Verification rounds used:** 2
**Scope detail:** each join input has exactly one column, and the join condition is a single uninterpreted predicate over the join's two-column row


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinCommuteRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

before() is Join(L, R, INNER, P(l, r)) and after() is Join(R, L, INNER, P(l, r)) with the references correctly remapped to positions (1, 0) (via right.joinField(1, left) / right.joinField(0, left)) followed by Project(l, r) to restore column order — exactly Calcite's VariableReplacer plus column-restoring project for the default INNER-only config, with precisely one shared predicate symbol and two distinct table symbols, so the proof is non-vacuous (a swapped P argument order, a missing project, or two independent predicate symbols would not have proved). The disclosed PARTIAL restrictions are genuine and specific: one-column-per-side is forced by the DSL's single-column Scan, and a single uninterpreted 2-ary predicate is actually the most general single-atom condition over the two-column row (any left-only or right-only condition is an instance of it). The source's self-join exclusion is a planner heuristic (swapping a self-join yields an identical tree), not a semantic precondition, so no essential assumption is silently dropped. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6817332
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 32386292
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 848666
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 440042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18878875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 32473292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 66898375
  }
}
```
