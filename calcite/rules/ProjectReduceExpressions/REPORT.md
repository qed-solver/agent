# ProjectReduceExpressions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** the project expression is FALSE AND an uninterpreted predicate, reduced to the constant FALSE


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, JoinReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

This is a genuine, non-vacuous special case of the real rule: ProjectReduceExpressionsRule's simplification pass (RexSimplify.simplifyPreservingType) does in fact rewrite a project expression like FALSE AND P(x) to the literal FALSE, and QED can only prove Project([FALSE ∧ P]) ≡ Project([FALSE]) by using its actual three-valued AND semantics (had AND been modeled as uninterpreted, the proof would fail), so the result is not trivial. Nothing is illegitimately hard-coded: the predicate P is uninterpreted (no concrete "x > 5" baked in), the same Source symbol is correctly shared between before() and after(), and the source rule carries no side preconditions (no keys/NOT NULL) that the encoding silently drops — the matchNullability cast the real rule would add is semantically inert on a literal. The narrowing to a purely-logical constant is forced by QED's inability to evaluate concrete operator algebra (the full rule's constant folding of e.g. 1+2 is a fundamental QED limitation, not a missing DSL feature), and the SCOPE: PARTIAL line states the assumption accurately and specifically. ```

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
    "nanos": 351667
  }
}
```
