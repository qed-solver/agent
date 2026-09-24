# Name: EliminateDistinctSetLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/set.opt

EliminateDistinctSetLeft replaces a Union or Except operator with a right side
having a cardinality of zero, with a Distinct on just the left side operand.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateDistinctSetLeft`, not the other rules in that file):

```
# EliminateDistinctSetLeft replaces a Union or Except operator with a right side
# having a cardinality of zero, with a Distinct on just the left side operand.
[EliminateDistinctSetLeft, Normalize]
(Union | Except
    $left:*
    $right:* & (HasZeroRows $right)
    $colMap:*
)
=>
(DistinctOn
    $project:(Project
        $left
        (ProjectColMapLeft $colMap)
        (ProjectPassthroughLeft $colMap)
    )
    []
    (MakeGrouping (OutputCols $project) (EmptyOrdering))
)
```
