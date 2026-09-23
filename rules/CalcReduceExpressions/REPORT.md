# CalcReduceExpressions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** the Calc's condition is the constant FALSE literal (the branch where constant reduction of the condition yields a constant false, so the whole Calc is replaced by an empty relation with the Calc's output row type); general constant folding of projections/conditions is not modelable with uninterpreted symbols


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `CalcReduceExpressionsRule` variant (not FilterReduceExpressionsRule, JoinReduceExpressionsRule, ProjectReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the Calc branch where the condition is constant FALSE: before is a FALSE-filtered source projected by E1/E2, while after is an empty relation whose row type is taken from the same E1/E2 projection. The PARTIAL scope honestly identifies the real semantic restriction; the fixed two-column/two-expression arity is only the concrete RuleScript arity instance, and the proof is non-vacuous because before still contains the false filter/project.

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
    "nanos": 782458
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 208792
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
    "nanos": 1367125
  }
}
```
