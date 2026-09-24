# Name: FoldNotFalse
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

FoldNotFalse replaces NOT(False) with True.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNotFalse`, not the other rules in that file):

```
# FoldNotFalse replaces NOT(False) with True.
[FoldNotFalse, Normalize]
(Not (False))
=>
(True)
```
