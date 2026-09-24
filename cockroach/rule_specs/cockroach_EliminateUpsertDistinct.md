# Name: EliminateUpsertDistinct
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateUpsertDistinct is similar to EliminateDistinct, but it only checks if
the grouping columns are a lax key because UpsertDistinctOn considers NULL
values to be distinct from one another for the purposes of grouping.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateUpsertDistinct`, not the other rules in that file):

```
# EliminateUpsertDistinct is similar to EliminateDistinct, but it only checks if
# the grouping columns are a lax key because UpsertDistinctOn considers NULL
# values to be distinct from one another for the purposes of grouping.
[EliminateUpsertDistinct, Normalize]
(UpsertDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggs:*
    $groupingPrivate:* &
        (ColsAreLaxKey (GroupingCols $groupingPrivate) $input)
)
=>
(Project $input [] (GroupingOutputCols $groupingPrivate $aggs))
```
