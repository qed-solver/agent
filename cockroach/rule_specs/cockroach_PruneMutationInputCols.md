# Name: PruneMutationInputCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneMutationInputCols discards input columns that are never used by the
mutation operator. This is high priority so that it runs before
UseSwapMutation, UseSwapMutationWithProjection, and
UseSwapMutationWithProjectionProjection.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneMutationInputCols`, not the other rules in that file):

```
# PruneMutationInputCols discards input columns that are never used by the
# mutation operator. This is high priority so that it runs before
# UseSwapMutation, UseSwapMutationWithProjection, and
# UseSwapMutationWithProjectionProjection.
[PruneMutationInputCols, Normalize, HighPriority]
(Update | Upsert | Delete
    $input:*
    $uniqueChecks:*
    $fkChecks:*
    $mutationPrivate:* &
        (CanPruneCols
            $input
            $needed:(NeededMutationCols
                $mutationPrivate
                $uniqueChecks
                $fkChecks
            )
        )
)
=>
((OpName)
    (PruneCols $input $needed)
    $uniqueChecks
    $fkChecks
    $mutationPrivate
)
```
