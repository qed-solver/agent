# SemiJoinProjectTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 80  **Verification rounds used:** 5
**Scope detail:** only the semi-join variant of the rule (the Calcite rule also fires on anti-joins); left projection is an arbitrary multi-expression project, join condition an uninterpreted predicate over the projected row


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinProjectTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the rule's semi-join branch: before() = SemiJoin(Project([P0,P1], X), Y, C) and after() = Project([P0,P1], SemiJoin(X, Y, C')), with the same uninterpreted P0, P1, C symbols shared across both patterns and the condition correctly "adjusted" — in both patterns C is evaluated over the values (P0(x0,x1), P1(x0,x1), y0, y1), exactly matching `adjustCondition`'s substitution of the projection expressions for the LHS references — and the two plans are structurally different, so the proof is non-vacuous; the SEMI join kind is correctly preserved on both sides (and unlike an inner join, the per-row existence correspondence keeps bags equal). The only restriction relative to the source — semi-joins only, whereas Calcite also fires on anti-joins — is specific and honestly declared in the `SCOPE: PARTIAL` line, and the fixed two-column arity is an inherent fixed-arity DSL artifact that constrains no uninterpreted logic; allowing C to reference the left (projected) columns, which Calcite itself forbids, only makes the proven claim stronger, not weaker.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8713623
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35253666
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 883708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 583958
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22490666
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35388500
  },
  "total_duration": {
    "secs": 0,
    "nanos": 73750625
  }
}
```
