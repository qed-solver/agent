# Name: EliminateZeroCardProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/project.opt

EliminateZeroCardProject eliminates a Project when its input has zero
cardinality. The projection expressions are per-row and will never be
evaluated, so it is safe to remove them regardless of volatility. This
complements the SimplifyZeroCardinalityGroup rule which requires the entire
expression to be leakproof.

Extracted from `project.opt` (which defines multiple rules — implement specifically `EliminateZeroCardProject`, not the other rules in that file):

```
# EliminateZeroCardProject eliminates a Project when its input has zero
# cardinality. The projection expressions are per-row and will never be
# evaluated, so it is safe to remove them regardless of volatility. This
# complements the SimplifyZeroCardinalityGroup rule which requires the entire
# expression to be leakproof.
[EliminateZeroCardProject, Normalize]
(Project
    $input:* & (HasZeroRows $input) & (IsLeakproof $input)
    $projections:*
    $passthrough:*
)
=>
(ConstructEmptyValues
    (UnionCols $passthrough (ProjectionCols $projections))
)
```
