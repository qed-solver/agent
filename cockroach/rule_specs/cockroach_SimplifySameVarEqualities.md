# Name: SimplifySameVarEqualities
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifySameVarEqualities converts `x = x` and other equality
comparisons into `x IS NOT NULL OR NULL`. The `OR NULL` is necessary
when x is NULL.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifySameVarEqualities`, not the other rules in that file):

```
# SimplifySameVarEqualities converts `x = x` and other equality
# comparisons into `x IS NOT NULL OR NULL`. The `OR NULL` is necessary
# when x is NULL.
[SimplifySameVarEqualities, Normalize]
(Eq | Le | Ge
    $left:(Variable)
    $right:(Variable) & (VarsAreSame $left $right)
)
=>
(Or (IsNot $left (Null (TypeOf $left))) (Null (BoolType)))
```
