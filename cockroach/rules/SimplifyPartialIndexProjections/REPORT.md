# SimplifyPartialIndexProjections

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/mutation.opt

SimplifyPartialIndexProjections converts partial index PUT and DEL projected
expressions to false when it is guaranteed that the mutation will not require
changes to the associated partial index. These projected expressions can only
be simplified to false when an UPDATE mutates neither the associated index's
columns nor the columns referenced in the partial index predicate.

Extracted from `mutation.opt` (which defines multiple rules — implement specifically `SimplifyPartialIndexProjections`, not the other rules in that file):

```
# SimplifyPartialIndexProjections converts partial index PUT and DEL projected
# expressions to false when it is guaranteed that the mutation will not require
# changes to the associated partial index. These projected expressions can only
# be simplified to false when an UPDATE mutates neither the associated index's
# columns nor the columns referenced in the partial index predicate.
[SimplifyPartialIndexProjections, Normalize]
(Update
    $project:(Project $input:* $projections:* $passthrough:*)
    $uniqueChecks:*
    $fkChecks:*
    $mutationPrivate:* &
        ^(ColsAreEmpty
            $simplifiableCols:(SimplifiablePartialIndexProjectCols
                $mutationPrivate
                $uniqueChecks
                $fkChecks
                $projections
            )
        )
)
=>
(Update
    (Project
        $input
        (Let
            (
                $simplifiedProjections
                $simplifiedPrivate
            ):(SimplifyPartialIndexProjections
                $projections
                $passthrough
                $simplifiableCols
                $mutationPrivate
            )
            $simplifiedProjections
        )
        $passthrough
    )
    $uniqueChecks
    $fkChecks
    $simplifiedPrivate
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule operates on CockroachDB's `Update` mutation operator (absent from QED's query-algebra model) under a side condition that is a structural/schema-level column-set analysis (`ColsAreEmpty(SimplifiablePartialIndexProjectCols(...))`), not a data-level relational predicate; no DSL extension can close this gap because QED's SMT prover reasons exclusively over bag-semantic query plans with uninterpreted functions and has no notion of DML side effects or plan-metadata column-set reasoning, so the constant-folding obligation (projected partial-index flag → `false`) is fundamentally unexpressible and unprovable in this framework. ```
