# Name: FoldIsNotNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldIsNotNull replaces NULL IS NOT NULL with False.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldIsNotNull`, not the other rules in that file):

```
# FoldIsNotNull replaces NULL IS NOT NULL with False.
[FoldIsNotNull, Normalize]
(IsNot (Null) (Null))
=>
(False)
```
