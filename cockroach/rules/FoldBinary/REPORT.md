# FoldBinary

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** restricts FoldBinary to the boolean AND operator over the constant literals TRUE and FALSE in a filter position, the only typed-constant/operator combination QED has semantics for (it cannot evaluate arbitrary uninterpreted operators over typed constants).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldBinary evaluates a binary operation over constant inputs, replacing the
entire expression with a constant. The rule applies as long as the evaluation
would not cause an error. Any errors should be saved for execution time,
since it's possible that the given operation will not be executed. For
example:

SELECT CASE WHEN true THEN 42 ELSE 1/0 END

In this query, the ELSE clause is not executed, so the divide-by-zero error
should not be triggered.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldBinary`, not the other rules in that file):

```
# FoldBinary evaluates a binary operation over constant inputs, replacing the
# entire expression with a constant. The rule applies as long as the evaluation
# would not cause an error. Any errors should be saved for execution time,
# since it's possible that the given operation will not be executed. For
# example:
#
#   SELECT CASE WHEN true THEN 42 ELSE 1/0 END
#
# In this query, the ELSE clause is not executed, so the divide-by-zero error
# should not be triggered.
[FoldBinary, Normalize]
(Binary
    $left:* & (IsConstValueOrGroupOfConstValues $left)
    $right:* &
        (IsConstValueOrGroupOfConstValues $right) &
        (Let
            ($result $ok):(FoldBinary (OpName) $left $right) $ok
        )
)
=>
$result
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-vacuous special case whose before/after shapes exactly match one concrete firing of the original FoldBinary — an AND of the constant literals TRUE and FALSE in a filter position, folded to the constant FALSE — and the SCOPE line honestly and specifically names that restriction. The narrowing to boolean AND over literals is forced by a real QED limitation rather than a missed DSL capability: QED has no uninterpreted constant data values and treats all non-boolean operators as uninterpreted, so it fundamentally cannot express "fold an arbitrary operator over arbitrary constants to a fresh constant," making this the narrowest faithful instance expressible. The remaining failure modes are clean — the proof is not vacuous (the AND is actually eliminated), the shared `Source` relation is shared correctly across both sides, and the rule's `IsConstValueOrGroupOfConstValues`/`$ok` preconditions are vacuously satisfied for these literals, so the "provable" result is meaningful within its stated scope. ```

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
    "nanos": 311042
  }
}
```
