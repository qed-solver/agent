# FoldNonNullIsNotNull

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 2
**Scope detail:** public record FoldNonNullIsNotNull() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullIsNotNull replaces x IS NOT NULL with True where x is a non-Null constant.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullIsNotNull`, not the other rules in that file):

```
# FoldNonNullIsNotNull replaces x IS NOT NULL with True where x is a non-Null constant.
[FoldNonNullIsNotNull, Normalize]
(IsNot $left:(IsNeverNull $left) (Null))
=>
(True)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful relational lifting of the scalar rule `IsNot $left (Null) => True` under `IsNeverNull $left`: `before()` is `Filter(x IS NOT NULL, S)` and `after()` is `Filter(TRUE, S)` over an uninterpreted scan whose column is declared non-nullable — which is exactly the source rule's side condition, since "never null" (the property that makes the fold valid, whether x is a constant or not) is expressed in this DSL precisely as a non-nullable column type, and no concrete type, predicate, or constant is baked in. The two sides are structurally and semantically distinct in general (without the nullability annotation the equivalence would fail), so the fast, clean proof is non-vacuous — it can only go through by using the encoded `IsNeverNull` guarantee, i.e., it proves exactly the rule's content. SCOPE: FULL is honest: the only constraints in the encoding (argument is a top-level column, the expression is the whole filter condition) are the canonical embedding of a scalar rewrite in RuleScript (as in the FilterMerge example), not semantic assumptions beyond the original rule, and the result is a genuine, useful no-op-filter elimination rather than a degenerate one. ```

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
    "nanos": 405583
  }
}
```
