# Name: FoldNullAndOr
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

FoldNullAndOr replaces the operator with null if both operands are null.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNullAndOr`, not the other rules in that file):

```
# FoldNullAndOr replaces the operator with null if both operands are null.
[FoldNullAndOr, Normalize]
(And | Or (Null) (Null))
=>
(Null (BoolType))
```
