# Name: EliminateDistinctSetRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/set.opt

EliminateDistinctSetRight mirrors EliminateDistinctSetLeft. Note that it only
applies to Union because Except operators only output left input rows.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateDistinctSetRight`, not the other rules in that file):

```
# EliminateDistinctSetRight mirrors EliminateDistinctSetLeft. Note that it only
# applies to Union because Except operators only output left input rows.
[EliminateDistinctSetRight, Normalize]
(Union $left:* & (HasZeroRows $left) $right:* $colMap:*)
=>
(DistinctOn
    $project:(Project
        $right
        (ProjectColMapRight $colMap)
        (ProjectPassthroughRight $colMap)
    )
    []
    (MakeGrouping (OutputCols $project) (EmptyOrdering))
)
```
