# Name: FoldEqFalse
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldEqFalse replaces x = False with NOT x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldEqFalse`, not the other rules in that file):

```
# FoldEqFalse replaces x = False with NOT x.
[FoldEqFalse, Normalize]
(Eq $left:* (False))
=>
(Not $left)
```
