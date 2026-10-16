# FoldNullBinaryRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** the binary operator is instantiated as the concrete `<` (null right arg implies null result, the rule's not-AllowNullArgs case), since QED only models null propagation for concrete primitive operators and arithmetic operators cannot infer return types over the virtual operand type V


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldNullBinaryRight replaces the binary operator with null if its right input
is null and it does not allow null arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldNullBinaryRight`, not the other rules in that file):

```
# FoldNullBinaryRight replaces the binary operator with null if its right input
# is null and it does not allow null arguments.
[FoldNullBinaryRight, Normalize]
(Binary
    $left:*
    $right:(Null) & ^(AllowNullArgs (OpName) $left $right)
)
=>
(FoldNullBinary (OpName) $left $right)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a non-vacuous, faithful instance of the rule: `before()` is `Filter(R, x < NULL)` and `after()` is `Filter(R, NULL-literal)`, which are structurally different and whose bag-equivalence depends precisely on the null-propagation property (`<` does not allow null args, so `x < NULL` is never true) that `FoldNullBinaryRight` relies on, with an arbitrary left value `x` standing for `$left:*` and a genuine null literal for `$right:(Null)`, in filter position — a legitimate embedding of the expression-level rewrite with the result type forced to boolean. Hard-coding `<` rather than an uninterpreted binary operator is a genuine QED limitation (uninterpreted symbols carry no null semantics, and the prover cannot be modified to grant them), not a missing DSL capability, and the `SCOPE: PARTIAL` line honestly and specifically names exactly this restriction, leaving the result a real, non-degenerate rewrite.

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
    "nanos": 335042
  }
}
```
