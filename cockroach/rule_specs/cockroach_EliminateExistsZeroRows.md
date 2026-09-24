# Name: EliminateExistsZeroRows
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsZeroRows converts an Exists subquery to False when it's known
that the input produces zero rows.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsZeroRows`, not the other rules in that file):

```
# EliminateExistsZeroRows converts an Exists subquery to False when it's known
# that the input produces zero rows.
[EliminateExistsZeroRows, Normalize]
(Exists $input:* & (HasZeroRows $input))
=>
(False)
```
