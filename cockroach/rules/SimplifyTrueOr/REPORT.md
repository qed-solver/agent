# SimplifyTrueOr

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** public record SimplifyTrueOr() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyTrueOr maps the Or operator to True if its left input is True.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyTrueOr`, not the other rules in that file):

```
# SimplifyTrueOr maps the Or operator to True if its left input is True.
[SimplifyTrueOr, Normalize]
(Or $left:(True) *)
=>
$left
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures CockroachDB's `SimplifyTrueOr` rule (`Or(True, *) → True`) by using `RexRN.trueLiteral()` for the literal `True`, an uninterpreted predicate `right` for the arbitrary right operand (`*`), and a `Filter` as the minimal relational embedding for the scalar rewrite. The `before()` (`Filter(Or(True, right), S)`) and `after()` (`Filter(True, S)`) are structurally distinct, so the proof is non-vacuous, and no assumptions beyond the source rule are introduced, making `SCOPE: FULL` correct.

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
    "nanos": 457958
  }
}
```
