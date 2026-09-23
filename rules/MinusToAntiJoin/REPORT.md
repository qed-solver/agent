# MinusToAntiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the 2-input, single-column instance in which both inputs share one row type, so the rule's type-unification casts are identity (the general n-way rule is obtained by repeated application of this binary step).


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToAntiJoinRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding reproduces the rule's 2-way transformation exactly — set-minus (all=false) rewritten to an ANTI join on IS NOT DISTINCT FROM followed by a group-by-all (the rule's final DISTINCT), with correct operand direction and with both of the rule's preconditions (not all, ≥2 inputs) respected — and its restrictions (2-input, single-column, shared row type so the type-unification cast is identity) are precisely declared in the SCOPE line, are non-vacuous (structurally different plans), and coincide with the rule's own documented 2-way example; the n-way case reduces soundly to repeated composition of this binary step, as the rule's javadoc itself states, and the shared type symbol is harmless since QED erases all VarTypes to a single integer type.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9813583
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 26003875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 923375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 491334
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24402583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 26092834
  },
  "total_duration": {
    "secs": 0,
    "nanos": 66095083
  }
}
```
