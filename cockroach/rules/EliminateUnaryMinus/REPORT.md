# EliminateUnaryMinus

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** package org.qed.RRuleInstances;


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

EliminateUnaryMinus discards a doubled UnaryMinus operator.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `EliminateUnaryMinus`, not the other rules in that file):

```
# EliminateUnaryMinus discards a doubled UnaryMinus operator.
[EliminateUnaryMinus, Normalize]
(UnaryMinus (UnaryMinus $input:*))
=>
$input
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source rule's shape: before() is Project(UNARY_MINUS(UNARY_MINUS(col)), Scan) vs after() = Scan — structurally different (so the proof is not vacuous), with the input universally quantified over arbitrary rows of a nullable, uninterpreted-type table, exactly matching the guard-free, universally-quantified $input of the original. Reusing the concrete SqlStdOperatorTable.UNARY_MINUS in both positions is essential and correct, not under-generalization: --x = x holds only for the *same* real negation operator (an uninterpreted symbol would make the claim false and unprovable), so QED's successful proof necessarily reflects its genuine arithmetic semantics of negation, which coincides with CockroachDB's UnaryMinus. No preconditions are missing (the source rule has no guards, and double negation is an involution even for NULL), and the input's

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
    "nanos": 285917
  }
}
```
