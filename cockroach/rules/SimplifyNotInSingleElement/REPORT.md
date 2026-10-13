# SimplifyNotInSingleElement

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** public record SimplifyNotInSingleElement() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyNotInSingleElement`, not the other rules in that file):

```
[SimplifyNotInSingleElement, Normalize]
(NotIn $left:* (Tuple [ $right:* ]))
=>
(Ne $left $right)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful relational statement of the scalar rule: before() builds `left NOT IN (right)` as filter `NOT(left = right)` (single-element IN = one-arg Or of `left = right`, then Not), and after() builds `Ne` as filter `left <> right`, so QED's universal proof that these filters agree over all contents of a two-column table is exactly the semantic validity of the CockroachDB rewrite. left and right are two *distinct* uninterpreted columns of a common uninterpreted type V (no over-constraint), and no precondition is missing because `NOT(x = y)` and `x <> y` share the same three-valued truth table including the NULL case; the only adaptation—embedding the scalar rewrite inside a Filter—is a necessary relational-DSL artifact rather than a semantic narrowing, so the `SCOPE: FULL` tag is honest. ```

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
    "nanos": 360166
  }
}
```
