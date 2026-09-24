# Name: FoldNotTrue
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

FoldNotTrue replaces NOT(True) with False.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNotTrue`, not the other rules in that file):

```
# FoldNotTrue replaces NOT(True) with False.
[FoldNotTrue, Normalize]
(Not (True))
=>
(False)
```
