# DecorrelateJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** INNER join only; condition is a single uninterpreted binary predicate over one left column and one right column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

DecorrelateJoin maps an apply join into the corresponding join without an
apply if the right side of the join is not correlated with the left side.
This allows the optimizer to consider additional physical join operators that
are unable to handle correlated inputs.

NOTE: Keep this before other decorrelation patterns, as if the correlated
join can be removed first, it avoids unnecessarily matching other
patterns that only exist to get to this pattern.

Citations: [3]

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `DecorrelateJoin`, not the other rules in that file):

```
# DecorrelateJoin maps an apply join into the corresponding join without an
# apply if the right side of the join is not correlated with the left side.
# This allows the optimizer to consider additional physical join operators that
# are unable to handle correlated inputs.
#
# NOTE: Keep this before other decorrelation patterns, as if the correlated
#       join can be removed first, it avoids unnecessarily matching other
#       patterns that only exist to get to this pattern.
#
# Citations: [3]
[DecorrelateJoin, Normalize]
(JoinApply
    $left:*
    $right:* & ^(IsCorrelated $right (OutputCols $left))
    $on:*
    $private:*
)
=>
(ConstructNonApplyJoin (OpName) $left $right $on $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core DecorrelateJoin transformation: `before()` builds a `LogicalCorrelate` (INNER) where the right side is filtered by a condition referencing the left row via correlated variables, and `after()` builds a `LogicalJoin` (INNER) with the same uninterpreted predicate over both sides' fields. The right input is a plain scan with no outer references, correctly satisfying the original rule's `^(IsCorrelated $right ...)` precondition. QED is verifying a genuine semantic equivalence between two structurally distinct operators (serialized as `correlate` vs. `join` in the JSON), so the proof is non-vacuous. The PARTIAL tag honestly states the two real restrictions (INNER only; binary predicate over one column per side), both of which are genuine narrowing versus the original rule's `(OpName)` / arbitrary-column generality. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9457209
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36544459
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 971333
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 647542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24386250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36667500
  },
  "total_duration": {
    "secs": 0,
    "nanos": 78528417
  }
}
```
