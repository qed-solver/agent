# OrFalseIdentity

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** public record OrFalseIdentity() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 960-965
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding differs from `after()` in exactly the intended way — `Filter(OR(falseLiteral, P), source)` vs `Filter(P, source)` — where `P` is a genuinely uninterpreted predicate symbol and `source` is an uninterpreted scan, so the proof covers the full family `false OR A → A` rather than a hard-coded instance. The literal-false left operand and two-argument OR match the DataFusion guard `is_false(&left)` exactly, and no precondition (uniqueness, NOT NULL, etc.) is silently assumed — the disjunction identity even holds pointwise under three-valued null semantics, and the single-column scan is a neutral carrier rather than a restriction, since the boolean identity is independent of the source's shape. ```

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
    "nanos": 386458
  }
}
```
