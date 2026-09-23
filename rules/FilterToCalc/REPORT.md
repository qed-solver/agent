# FilterToCalc

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** public record FilterToCalc() implements RRule {


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterToCalcRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding captures exactly what FilterToCalcRule rewrites: `before()` is the bare `Filter(cond, R)`, and `after()` is the Calc the rule builds (RexProgramBuilder `addIdentity()` + `addCondition()`), which — since the DSL has no Calc operator — is correctly expressed under the project-based Calc convention as the filter plus an explicit identity projection over `source.fields()`. The condition is a single uninterpreted predicate symbol shared correctly between both sides, the input is an uninterpreted scan standing for an arbitrary relation, and the structural delta (the added identity project) mirrors the real before/after shape difference rather than being an accidental tautology. The equivalence is semantically trivial only because this rule is inherently a pure representation change (its stated guards — no subquery, non-Filter/Project/Calc child — are planner-firing strategy, not semantic preconditions the proof depends on), so the SCOPE: FULL tag is honest and the proof faithfully validates the rule's soundness claim.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 381833
  }
}
```
