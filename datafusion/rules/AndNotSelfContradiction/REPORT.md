# AndNotSelfContradiction

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** public record AndNotSelfContradiction() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1068-1075
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-trivial and faithful — `before()` filters on the uninterpreted predicate `A AND NOT(A)` while `after()` filters on the literal `false`, and QED shows both yield the empty bag for every instantiation of the uninterpreted `A`, with the *same* symbol `a` correctly shared between the conjunct and its negation (matching the source's `is_not_of(right, left)` self-contradiction constraint; two independent predicates would not be provable). The source's `!info.nullable(&left)` precondition is not silently dropped in a way that proves an easier or false claim: in a filter position `A AND NOT(A)` excludes every row exactly like `false` regardless of nullability (in both 2-valued and SQL 3-valued semantics), so nothing the proven claim needs is missing. The filter embedding with a fully uninterpreted predicate is therefore the complete relational form of this boolean-condition simplification, `before()`/`after()` are structurally distinct and non-degenerate, and `SCOPE: FULL` is honest.

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
    "nanos": 509917
  }
}
```
