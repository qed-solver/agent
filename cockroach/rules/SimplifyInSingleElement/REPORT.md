# SimplifyInSingleElement

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 2
**Scope detail:** public record SimplifyInSingleElement() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyInSingleElement`, not the other rules in that file):

```
[SimplifyInSingleElement, Normalize]
(In $left:* (Tuple [ $right:* ]))
=>
(Eq $left $right)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding models `x IN (y)` by its definitional expansion — a single-disjunct `Or(x = y)` — in `before()`, and the bare `Eq` in `after()`; since the DSL has no `IN` operator and an uninterpreted `IN` symbol could never be proven equivalent to an independent `Eq` symbol, proving `Filter(Or(eq)) ≡ Filter(eq)` is exactly the non-vacuous semantic content of the source rule (single-element IN ≡ equality, valid in three-valued logic with no added preconditions). `before()` and `after()` are structurally different (Or-wrapper vs bare predicate), so the proof is not vacuous; the two operands are arbitrary columns of a shared uninterpreted nullable type, so no relationship between `$left` and `$right` is assumed, and the filter-over-scan embedding is the standard representative of this position-independent scalar rule, making the FULL scope tag honest. ```

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
    "nanos": 349458
  }
}
```
