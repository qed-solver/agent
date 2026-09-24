# Name: SimplifySameVarInequalities
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifySameVarInequalities converts `x != x` and other inequality
comparisons into `x IS NULL AND NULL`. The `AND NULL` is necessary
when x is NULL.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifySameVarInequalities`, not the other rules in that file):

```
# SimplifySameVarInequalities converts `x != x` and other inequality
# comparisons into `x IS NULL AND NULL`. The `AND NULL` is necessary
# when x is NULL.
[SimplifySameVarInequalities, Normalize]
(Ne | Lt | Gt
    $left:(Variable)
    $right:(Variable) & (VarsAreSame $left $right)
)
=>
(And (Is $left (Null (TypeOf $left))) (Null (BoolType)))
```
