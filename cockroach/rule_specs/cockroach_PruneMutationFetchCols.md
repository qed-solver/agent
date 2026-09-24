# Name: PruneMutationFetchCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneMutationFetchCols removes columns from the mutation operator's FetchCols
set if they are never used. Removing FetchCols can in turn can trigger the
PruneMutationInputCols rule, which can prune any input columns which are now
unreferenced.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneMutationFetchCols`, not the other rules in that file):

```
# PruneMutationFetchCols removes columns from the mutation operator's FetchCols
# set if they are never used. Removing FetchCols can in turn can trigger the
# PruneMutationInputCols rule, which can prune any input columns which are now
# unreferenced.
[PruneMutationFetchCols, Normalize]
(Update | Upsert | Delete
    $input:*
    $uniqueChecks:*
    $fkChecks:*
    $mutationPrivate:* &
        (CanPruneMutationFetchCols
            $mutationPrivate
            $needed:(NeededMutationFetchCols
                (OpName)
                $mutationPrivate
            )
        )
)
=>
((OpName)
    $input
    $uniqueChecks
    $fkChecks
    (PruneMutationFetchCols $mutationPrivate $needed)
)
```
