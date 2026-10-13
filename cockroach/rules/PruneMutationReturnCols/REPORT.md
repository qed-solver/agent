# PruneMutationReturnCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

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

**Verdict:** AGREE

The rule's only semantic effect is narrowing the ReturnCols set stored in the mutation operator's private state, and RuleScript's core language has no construct for a mutation operator or its operator-private column-set parameter — so any encoding either reuses one uninterpreted scan on both sides (before() ≡ after(), a vacuous reflexivity) or introduces two independent scans that QED cannot relate, and modeling the pruned mutation as an explicit projection of the full mutation would merely prove the generic projection-composition law while taking the mutation-specific "narrowing ReturnCols to a superset of used columns preserves visible output" property as an unverified axiom. A DSL extension cannot close this gap because QED's own (immutable) semantics have no notion of a mutation or of per-operator column sets — its JSON format and semiring translation only define scan/filter/project/join/aggregate/set operators — so the rule rests on a backend operator's bespoke internal semantics that QED fundamentally cannot see through. This is a genuine QED/language limitation, not a missed encoding, and the porter's analysis of all three encoding routes is correct. ```
