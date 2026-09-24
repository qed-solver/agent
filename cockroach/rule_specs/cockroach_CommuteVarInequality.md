# Name: CommuteVarInequality
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

CommuteVarInequality is similar to CommuteVar (in scalar.opt), except it
handles inequality comparison operators that need special handling to commute
operands.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteVarInequality`, not the other rules in that file):

```
# CommuteVarInequality is similar to CommuteVar (in scalar.opt), except it
# handles inequality comparison operators that need special handling to commute
# operands.
[CommuteVarInequality, Normalize]
(Le | Lt | Ge | Gt $left:^(Variable) $right:(Variable))
=>
(CommuteInequality (OpName) $left $right)
```
