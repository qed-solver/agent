# OrTrueAbsorption

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** public record OrTrueAbsorption() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 966-971
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (`Filter(Or(<uninterpreted pred>, trueLiteral), S)`) and `after()` (`Filter(trueLiteral), S`) are structurally distinct, so the proof is non-vacuous; the arbitrary expression `A` of the source rule is modeled by a single fully-general uninterpreted predicate over a nullable column (no NOT-NULL or PK assumption baked in, matching the "even if A is null" case), and the right side is correctly fixed to the `true` literal per `is_true(&right)`, with the filter carrier being the standard faithful embedding of this scalar law in a relational prover. SCOPE: FULL is honest — nothing in the source rule's generality (any `A`, null included) is narrowed by the encoding.

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
    "nanos": 278250
  }
}
```
