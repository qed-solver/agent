# DoubleNegationElimination

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 15  **Verification rounds used:** 1
**Scope detail:** public record DoubleNegationElimination() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 339-340
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` is Filter(Not(Not(pred)), Source) and `after()` is Filter(pred, Source) — structurally and semantically distinct plans related by exactly the source rewrite `Expr::Not(Not(A)) => A`, with the source table, its row type, and the predicate all left as uninterpreted symbols, so the universal proof covers arbitrary relations and arbitrary predicates. The filter-over-scan shape is merely the canonical relational host for this purely expression-level Boolean law (no other context carries more content), and no preconditions are missing — ¬¬A and A coincide even under three-valued NULL semantics, since a filter keeps only rows whose condition is TRUE and ¬¬NULL = NULL. Thus the encoding is non-vacuous, free of accidental over-constraint, and the `// SCOPE: FULL` tag is honest. ```

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
    "nanos": 321292
  }
}
```
