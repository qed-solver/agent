# Name: FoldNonNullIsNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullIsNull replaces x IS NULL with False where x is a non-Null constant.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullIsNull`, not the other rules in that file):

```
# FoldNonNullIsNull replaces x IS NULL with False where x is a non-Null constant.
[FoldNonNullIsNull, Normalize]
(Is $left:(IsNeverNull $left) (Null))
=>
(False)
```
