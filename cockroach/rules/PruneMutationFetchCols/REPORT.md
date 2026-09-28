# PruneMutationFetchCols

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 7  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule only mutates the DML operator's private `FetchCols` metadata (a set of column indices the mutation fetches) while holding `$input`, `$uniqueChecks`, `$fkChecks` structurally identical and inserting no `Project`, so its before/after are relationally identical and its entire correctness content — "dropping never-used fetch columns preserves the mutation's behavior" — lives in CockroachDB's bespoke `CanPruneMutationFetchCols`/`NeededMutationFetchCols` operator-internal semantics. RuleScript's core language is a query-focused relational algebra with no mutation (`Update`/`Upsert`/`Delete`) operator and no notion of operator private metadata, and even modeling the mutation as an uninterpreted function wouldn't help: QED has no axiom making that function insensitive to pruning of unused fetch columns, so it would treat it as an arbitrary black box and could not prove `M(input, F) = M(input, F∩Needed)` — a fundamental bag-semantic limitation, not a DSL gap `extend_dsl_file` could close.
