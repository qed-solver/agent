# Name: FoldNonNullIsNotNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullIsNotNull replaces x IS NOT NULL with True where x is a non-Null constant.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullIsNotNull`, not the other rules in that file):

```
# FoldNonNullIsNotNull replaces x IS NOT NULL with True where x is a non-Null constant.
[FoldNonNullIsNotNull, Normalize]
(IsNot $left:(IsNeverNull $left) (Null))
=>
(True)
```
