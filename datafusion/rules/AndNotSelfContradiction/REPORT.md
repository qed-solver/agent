# AndNotSelfContradiction

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** public record AndNotSelfContradiction() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1068-1075
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` and `after()` are genuinely different plans (filter condition `A AND NOT(A)` vs. the `false` literal), the predicate `A` and the source table are properly uninterpreted, and the single shared symbol `a` is exactly right — the DataFusion guard `is_not_of(&right, &left)` requires the same subexpression on both sides, so the proof is non-vacuous and covers an arbitrary uninterpreted predicate over an arbitrary bag. The encoding does pin the pattern to the position of a Filter's *complete* condition rather than an arbitrary expression position (so `SCOPE: FULL` is a mild over-claim relative to the expression-level original), but that restriction is real, specific, and non-degenerate, and the source rule's non-nullable precondition is genuinely subsumed in this context: `A AND NOT(A)` is never TRUE even when `A` is NULL (a filter keeps only TRUE rows), so both filters are extensionally empty and the proved equivalence is the true, substantive content of the DataFusion rule in its canonical relational embedding.

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
    "nanos": 310500
  }
}
```
