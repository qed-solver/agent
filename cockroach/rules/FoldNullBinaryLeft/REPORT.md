# FoldNullBinaryLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 43  **Verification rounds used:** 3
**Scope detail:** the binary operator is instantiated as the concrete `<` (null left arg implies null result, the rule's not-AllowNullArgs case), embedded in filter position since QED only models null propagation for concrete primitive operators and arithmetic operators cannot infer return types over the virtual operand type V


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullBinaryLeft replaces the binary operator with null if its left input
is null and it does not allow null arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullBinaryLeft`, not the other rules in that file):

```
# FoldNullBinaryLeft replaces the binary operator with null if its left input
# is null and it does not allow null arguments.
[FoldNullBinaryLeft, Normalize]
(Binary
    $left:(Null)
    $right:* & ^(AllowNullArgs (OpName) $left $right)
)
=>
(FoldNullBinary (OpName) $left $right)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (filter on `NULL < x`) and `after()` (filter on the null literal) are structurally different, and QED's proof establishes a genuine, non-vacuous fact — that a null-left comparison is never TRUE in QED's three-valued model, so replacing it with a null condition preserves the bag, which is exactly what the rule asserts in filter position for `<`. The narrowing to a concrete primitive operator and filter context is forced by QED's inability to model null propagation for uninterpreted operators (the `!AllowNullArgs` condition cannot be axiomatized over them), and the `SCOPE: PARTIAL` line honestly and specifically names the assumed condition and its cause, so the result is a legitimate non-degenerate special case rather than a disguised triviality.

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
    "nanos": 78083
  }
}
```
