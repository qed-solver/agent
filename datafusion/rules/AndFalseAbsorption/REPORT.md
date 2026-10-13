# AndFalseAbsorption

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 10  **Verification rounds used:** 2
**Scope detail:** the rewrite is applied only when the false-AND conjunction is the filter condition


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1050-1055
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly captures the core algebraic identity of the DataFusion rule (`false AND P → false`) by using an uninterpreted predicate for the right operand and a literal `false` for the left, within a `Filter` context. The `before()` and `after()` are structurally distinct (conjunction vs. bare false), the proof is non-vacuous, and the PARTIAL scope marker is honest: the original rule is a general scalar-expression rewrite applicable in any expression context, but RuleScript's relational pattern language naturally restricts it to the filter-condition position on a base scan, which is the most common and practically useful instance. No symbol-sharing, operator, or precondition issues are present. ```

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
    "nanos": 177250
  }
}
```
