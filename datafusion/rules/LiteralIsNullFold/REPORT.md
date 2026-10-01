# LiteralIsNullFold

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** public record LiteralIsNullFold() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1801-1804
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful and non-vacuous: `before()` is `Filter(x IS NOT NULL, S)` vs `after()` = `Filter(true, S)` (structurally distinct conditions), and the critical side condition `!info.nullable(&expr)` is genuinely encoded via a non-nullable column type that JSONSerializer forwards to QED, so the proof rests on the value actually being non-nullable rather than ignoring the precondition. Modeling the non-nullable value as a universally-quantified scan column and proving only `IS NOT NULL` (which is semantically identical to `IS NOT UNKNOWN`) captures the rule's full logical content, so `SCOPE: FULL` is honest.

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
    "nanos": 256333
  }
}
```
