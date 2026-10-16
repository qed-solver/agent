# PruneMutationReturnCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 46  **Verification rounds used:** 4
**Scope detail:** the input row has exactly 4 columns and the mutation returns exactly 4 computed columns (each an uninterpreted function of all 4 input columns), one of which (a non-PK return column) is unused and pruned, one of which (a PK return column) is unused but retained, and the outer projection uses one return column through one computed expression plus one passthrough


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneReturningCols removes columns from the mutation operator's ReturnCols
set if they are not used in the RETURNING clause of the mutation.
Removing ReturnCols will then allow the PruneMutationFetchCols to be more
conservative with the fetch columns.
TODO(msirek): Mutations shouldn't need to return the primary key
columns. Investigate appropriate changes to SQL execution to accommodate this,
through #111733.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneMutationReturnCols`, not the other rules in that file):

```
# PruneReturningCols removes columns from the mutation operator's ReturnCols
# set if they are not used in the RETURNING clause of the mutation.
# Removing ReturnCols will then allow the PruneMutationFetchCols to be more
# conservative with the fetch columns.
# TODO(msirek): Mutations shouldn't need to return the primary key
# columns. Investigate appropriate changes to SQL execution to accommodate this,
# through #111733.
[PruneMutationReturnCols, Normalize]
(Project
    $input:(Update | Upsert | Delete
        $innerInput:*
        $uniqueChecks:*
        $fkChecks:*
        $mutationPrivate:*
    )
    $projections:*
    $passthrough:* &
        (CanPruneMutationReturnCols
            $mutationPrivate
            $needed:(UnionCols3
                (PrimaryKeyCols (MutationTable $mutationPrivate))
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    ((OpName $input)
        $innerInput
        $uniqueChecks
        $fkChecks
        (PruneMutationReturnCols $mutationPrivate $needed)
    )
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is structurally non-trivial (4-wide vs 3-wide mutation output with correct ordinal remapping of the shared passthrough column r3, field 3→field 2) and faithfully models the rule's distinctive content: an uninterpreted 1:1 row transformation standing in for Update/Upsert/Delete, one non-PK return column pruned, and one unused PK return column deliberately retained — matching the source rule's `PrimaryKeyCols ∪ ProjectionOuterCols ∪ passthrough` needed-set, so this is this specific rule rather than generic column pruning. The fixed 4-column arities and specific used/unused/PK pattern make it a genuine narrower instance, but that is a real DSL limitation (RuleScript records are concrete shapes with no arity generalization, and MetaAssignment only covers join types), and the scope line honestly states exactly that assumption with a specific condition, so the provable result is a faithful, non-degenerate special case. ```

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
    "nanos": 348292
  }
}
```
