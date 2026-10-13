# PruneInsertReturnCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneInsertReturnCols removes columns from the Insert operator's ReturnCols
set if they are not used in the RETURNING clause of the mutation.
Removing ReturnCols will then allow the PruneMutationFetchCols to be more
conservative with the fetch columns.
TODO(msirek): Mutations shouldn't need to return the primary key
columns. Investigate appropriate changes to SQL execution to accommodate this,
through #111733.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneInsertReturnCols`, not the other rules in that file):

```
# PruneInsertReturnCols removes columns from the Insert operator's ReturnCols
# set if they are not used in the RETURNING clause of the mutation.
# Removing ReturnCols will then allow the PruneMutationFetchCols to be more
# conservative with the fetch columns.
# TODO(msirek): Mutations shouldn't need to return the primary key
# columns. Investigate appropriate changes to SQL execution to accommodate this,
# through #111733.
[PruneInsertReturnCols, Normalize]
(Project
    $input:(Insert
        $innerInput:*
        $uniqueChecks:*
        $fastPathUniqueChecks:*
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
        $fastPathUniqueChecks
        $fkChecks
        (PruneMutationReturnCols $mutationPrivate $needed)
    )
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** AGREE

PruneInsertReturnCols changes only the Insert operator's internal MutationPrivate.ReturnCols metadata — operator-private state with no counterpart in RuleScript's relational core language — so in any faithful encoding before() and after() are relationally identical, and the rule's actual correctness claim (that a mutation's returned values for the retained columns don't depend on which other columns it is also configured to return) rests on CockroachDB's bespoke mutation-operator semantics, which QED's bag-semantic model cannot see through as uninterpreted symbols. The only available reduction (modeling the mutation's output as an uninterpreted scan/base relation) would let QED prove a generic "prune unneeded columns under a projection" law over arbitrary relations — a different, more general statement — while the specific precondition (CanPruneMutationReturnCols, needed = PK ∪ used ∪ passthrough ⊆ ReturnCols) and the metadata change itself remain inexpressible, so no genuine encodable special case of this rule exists.
