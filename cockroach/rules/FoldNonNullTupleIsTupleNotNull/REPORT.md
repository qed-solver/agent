# FoldNonNullTupleIsTupleNotNull

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 45  **Verification rounds used:** 3
**Scope detail:** the tuple is fixed to arity 2 and both of its elements are encoded as non-nullable (standing in for the constant, non-null elements required by the side condition HasAllNonNullElements).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullTupleIsTupleNotNull replaces x IS NOT NULL with True if x is a
tuple with only constant, non-null elements.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullTupleIsTupleNotNull`, not the other rules in that file):

```
# FoldNonNullTupleIsTupleNotNull replaces x IS NOT NULL with True if x is a
# tuple with only constant, non-null elements.
[FoldNonNullTupleIsTupleNotNull, Normalize]
(IsTupleNotNull $input:(Tuple) & (HasAllNonNullElements $input))
=>
(True)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous — `before()` filters on `(f0 IS NOT NULL) AND (f1 IS NOT NULL)` while `after()` filters on `true`, and the proof genuinely relies on QED's nullability modeling of the two `nullable=false` columns (the semantic content of HasAllNonNullElements), correctly expanding `IsTupleNotNull(x)` as the conjunction of its elements' `IS NOT NULL` predicates folding to `True`. Model

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
    "nanos": 100959
  }
}
```
