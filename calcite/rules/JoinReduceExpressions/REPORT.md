# JoinReduceExpressions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 32  **Verification rounds used:** 3
**Scope detail:** the join kind is fixed to INNER (the rule applies to all join kinds, but QED models the join kind concretely, not as an uninterpreted symbol) and the join condition is a conjunction of an uninterpreted predicate with the constant FALSE


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, ProjectReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors JoinReduceExpressionsRule's actual transformation — the rule rewrites via `join.copy(traitSet, reducedCond, left, right, joinType, semiJoinDone)`, i.e. the same join with a constant-folded condition and deliberately NO empty relation (unlike the Filter/Calc variants), which is exactly what after() encodes — and `C AND FALSE` → `FALSE` is a genuine, non-vacuous instance of constant reduction (RexSimplify folds an AND with a FALSE operand to FALSE) with the surviving predicate expressed as a shared uninterpreted symbol, so before() and after() are structurally different and the proof is real. The narrowing to INNER join kind and to this one reducible condition shape is exactly as disclosed in the SCOPE: PARTIAL line, and is unavoidable within the single-record contract since MetaJoinType serializes to INNER in the Calcite semantics QED sees, making the INNER instance the best achievable proof and a non-degenerate, faithful special case of the source rule. ```

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
    "nanos": 531750
  }
}
```
