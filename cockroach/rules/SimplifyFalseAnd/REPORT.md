# SimplifyFalseAnd

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** public record SimplifyFalseAnd() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyFalseAnd maps the And operator to False if its left input is False.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyFalseAnd`, not the other rules in that file):

```
# SimplifyFalseAnd maps the And operator to False if its left input is False.
[SimplifyFalseAnd, Normalize]
(And $left:(False) *)
=>
$left
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The before pattern is `Filter(Source, AND(false, right))` and after is `Filter(Source, false)`, with `right` an uninterpreted predicate and `Source` an arbitrary scan, faithfully matching `(And False *) => False`. It is nontrivial, uses the correct left-false `And` variant rather than the right-false sibling, and does not hard-code the arbitrary operand.

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
    "nanos": 297042
  }
}
```
