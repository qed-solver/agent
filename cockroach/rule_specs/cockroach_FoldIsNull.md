# Name: FoldIsNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldIsNull replaces NULL IS NULL with True.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldIsNull`, not the other rules in that file):

```
# FoldIsNull replaces NULL IS NULL with True.
[FoldIsNull, Normalize]
(Is (Null) (Null))
=>
(True)
```
