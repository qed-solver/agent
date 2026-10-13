# FoldUnary

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** restricts FoldUnary to the boolean NOT operator over the constant literal TRUE in filter position, the only typed-constant/operator combination QED has semantics for (it cannot evaluate arbitrary uninterpreted unary operators over typed constants).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldUnary is similar to FoldBinary, but it involves a unary operation over a
single constant input. As with FoldBinary, FoldUnary applies as long as the
evaluation would not cause an error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldUnary`, not the other rules in that file):

```
# FoldUnary is similar to FoldBinary, but it involves a unary operation over a
# single constant input. As with FoldBinary, FoldUnary applies as long as the
# evaluation would not cause an error.
[FoldUnary, Normalize]
(Unary
    $input:* &
        (IsConstValueOrGroupOfConstValues $input) &
        (Let ($result $ok):(FoldUnary (OpName) $input) $ok)
)
=>
$result
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` and `after()` are structurally distinct (`Not(trueLiteral())` vs `falseLiteral()`) and the equivalence proved is exactly the rule's content — a unary operator applied to a constant replaced by its folded constant — so the proof is not vacuous. The hard-coding of NOT/TRUE/FALSE is forced rather than lazy: QED treats arbitrary operators as uninterpreted and cannot know that `op(const)` equals any new constant, so this concrete boolean instance is the only provable shape (a DSL extension couldn't close the gap, since the prover is the fixed arbiter of operator semantics). The single-sentence `// SCOPE: PARTIAL` line accurately and specifically discloses the restriction (NOT over TRUE, filter position), and no precondition (constant input, successful evaluation `$ok`) is silently dropped for this instance, making it an honest, non-degenerate special case of FoldUnary.

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
    "nanos": 289000
  }
}
```
