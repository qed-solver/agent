# SimplifyPartialIndexProjections

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 84  **Verification rounds used:** 6

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

The rewrite is conditional on a static column-set side condition (the UPDATE writes none of the partial index's or predicate's columns), which RuleScript cannot express — QED decides bag equality over all instantiations of the uninterpreted symbols with no mechanism for side conditions — and with that guard dropped the rewrite is not bag-equivalent, since rows satisfying the partial predicate project (true,true) before but (false,false) after. The two outputs are interchangeable only inside CockroachDB's Mutation operator, whose put-XOR-del change detection treats pointwise-equal put/del as a no-op: that is a backend operator's bespoke internal semantics, and the DSL has no Update/Mutation operator QED could reason about — and any `extend_dsl_file` operator carrying that put/del-no-op property would axiomatize the very claim to be proved, so no faithful, non-vacuous encoding exists. ```
