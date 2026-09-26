# EliminateJoinUnderProjectLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 32  **Verification rounds used:** 2
**Scope detail:** the LEFT-join variant only, on a self-join of the same unique single non-nullable-column scan on equality of that column, with the project using only the preserved left column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/project.opt

EliminateJoinUnderProjectLeft replaces an InnerJoin or LeftJoin with its left
input when:
1. The project doesn't use columns from the join's right input.
2. The join does not duplicate any left rows.
3. The join does not filter any left rows.

Note: EliminateJoinUnderProjectLeft should stay above EliminateProject so that
it has a chance to fire before the Project can be removed.

It is possible for references to the right input of the join to be replaced by
equivalent columns from the left input. This is handled by adding projections
that map the left column to the equivalent right column (leftCol AS rightCol).

It is ok to call MergeProjections without checking CanMergeProjections because
RemapProjectionCols returns projections that only reference columns from the
left input, and ProjectRemappedCols only projects columns from the right side
of the join.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateJoinUnderProjectLeft`, not the other rules in that file):

```
# EliminateJoinUnderProjectLeft replaces an InnerJoin or LeftJoin with its left
# input when:
# 1. The project doesn't use columns from the join's right input.
# 2. The join does not duplicate any left rows.
# 3. The join does not filter any left rows.
#
# Note: EliminateJoinUnderProjectLeft should stay above EliminateProject so that
# it has a chance to fire before the Project can be removed.
#
# It is possible for references to the right input of the join to be replaced by
# equivalent columns from the left input. This is handled by adding projections
# that map the left column to the equivalent right column (leftCol AS rightCol).
#
# It is ok to call MergeProjections without checking CanMergeProjections because
# RemapProjectionCols returns projections that only reference columns from the
# left input, and ProjectRemappedCols only projects columns from the right side
# of the join.
[EliminateJoinUnderProjectLeft, Normalize]
(Project
    $join:(InnerJoin | LeftJoin $left:* $right:*) &
        (JoinDoesNotDuplicateLeftRows $join) &
        (JoinPreservesLeftRows $join)
    $projections:*
    $passthrough:* &
        (CanRemapCols
            $fromCols:(UnionCols
                $passthrough
                (ProjectionOuterCols $projections)
            )
            $leftCols:(OutputCols $left)
            $fds:(FuncDeps $join)
        ) &
        (CanUseImprovedJoinElimination $fromCols $leftCols)
)
=>
(Project
    $left
    (MergeProjections
        (RemapProjectionCols $projections $leftCols $fds)
        (ProjectRemappedCols $passthrough $leftCols $fds)
        $passthrough
    )
    (DifferenceCols $passthrough (OutputCols $right))
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (a LEFT self-join of a unique, non-nullable single-column scan on `col = col`, projecting only field 0) is structurally distinct from after() (the same column projected directly from the scan), and their bag-equivalence genuinely depends on the encoded key/uniqueness (no duplicate left rows), the non-null guarantee (so `a.col = a.col` holds cleanly, no NULL-equality gap), and the LEFT-join kind (no filtered left rows) — so the proof is non-vacuous and exercises the rule's actual preconditions rather than a tautology. The join kind (LEFT, one of the rule's two allowed kinds), the "project avoids the right side" condition, and the join-safety preconditions are all correctly reflected, and the residual narrowness (fixed LEFT kind, single-column unique-key self-join) is inherent to the fact that the source rule's general `JoinDoesNotDuplicateLeftRows`/functional-dependency/`CanRemapCols` machinery is inexpressible in this DSL — and it is honestly and specifically tagged `// SCOPE: PARTIAL`, so the "provable" result is neither meaningless nor misleading. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5119668
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35542667
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 882750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 467542
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 14973750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35606250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 66001125
  }
}
```
