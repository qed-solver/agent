# InvertMinus

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 66  **Verification rounds used:** 4
**Scope detail:** package org.qed.RRuleInstances;


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

InvertMinus rewrites -(a - b) to (b - a) if the operand types allow it.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `InvertMinus`, not the other rules in that file):

```
# InvertMinus rewrites -(a - b) to (b - a) if the operand types allow it.
[InvertMinus, Normalize]
(UnaryMinus
    (Minus $left:* $right:*) &
        (CanConstructBinary Minus $right $left)
)
=>
(Minus $right $left)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = Project[UNARY_MINUS(MINUS(a,b))] and after() = Project[MINUS(b,a)] over a shared, universally-quantified 2-column scan with independent fields a and b — a non-vacuous, structurally faithful rendering of `UnaryMinus(Minus $left $right) => Minus $right $left` using the actual MINUS/UNARY_MINUS operators and no symbol-sharing shortcut. The same-type INTEGER columns and the unexpressed CanConstructBinary guard (a meta-level type-constructibility check, tautological for same-type operands) cannot narrow the proven claim, since QED maps all types into one integer theory (per RelType.java's ground-truth note), so the proved identity ∀a,b: -(a-b) = b-a is exactly the rule's full value-level content, making SCOPE: FULL honest. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9253832
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37817667
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1724708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 395083
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19964459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37910916
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74509292
  }
}
```
