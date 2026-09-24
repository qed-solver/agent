# Name: FoldNeTrue
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNeTrue replaces x != True with NOT x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNeTrue`, not the other rules in that file):

```
# FoldNeTrue replaces x != True with NOT x.
[FoldNeTrue, Normalize]
(Ne $left:* (True))
=>
(Not $left)
```
