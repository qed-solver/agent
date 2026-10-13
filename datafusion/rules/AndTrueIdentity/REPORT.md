# AndTrueIdentity

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** public record AndTrueIdentity() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1044-1049
```

## Independent verifier review

**Verdict:** CONFIRMED

The before pattern is genuinely different from the after pattern because it contains `AND(trueLiteral, right)` under a filter, while the after pattern retains only the same uninterpreted predicate `right`. This captures the DataFusion law for an arbitrary boolean expression without adding a semantic precondition, and the shared `right` symbol correctly preserves the rewritten operand.

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
    "nanos": 91917
  }
}
```
