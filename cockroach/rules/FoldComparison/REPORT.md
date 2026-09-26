# FoldComparison

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** restricts FoldComparison to the equality comparison of the constant literals TRUE = FALSE folded to the constant FALSE in a filter position, the only typed-constant comparison QED has semantics for (the DSL can only express boolean literals and QED cannot evaluate operators over arbitrary typed constants).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldComparison is similar to FoldBinary, but it involves a comparison
operation. As with FoldBinary, FoldComparison applies as long as the
evaluation would not cause an error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldComparison`, not the other rules in that file):

```
# FoldComparison is similar to FoldBinary, but it involves a comparison
# operation. As with FoldBinary, FoldComparison applies as long as the
# evaluation would not cause an error.
[FoldComparison, Normalize]
(Comparison
    $left:* & (IsConstValueOrGroupOfConstValues $left)
    $right:* &
        (IsConstValueOrGroupOfConstValues $right) &
        (Let
            ($result $ok):(FoldComparison (OpName) $left $right)
            $ok
        )
)
=>
$result
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, honestly-labeled (PARTIAL) special case of FoldComparison: it picks the single concrete instance `TRUE = FALSE → FALSE` in a filter, which is the narrowest case where QED can actually evaluate a typed comparison over literals (the DSL exposes only boolean literals, and QED can only fold a comparison when both operands and the operator are concrete enough for it to compute the result). The hard-coding of EQUALS/TRUE/FALSE is not an unnecessary under-generalization—replacing any of them with an uninterpreted symbol would make the equivalence unprovable—so the scope restriction is genuine, specific, and the before/after are structurally distinct (a `Pred(EQUALS, true, false)` vs. a bare `false`), making the proof non-vacuous.

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
    "nanos": 304334
  }
}
```
