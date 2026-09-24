# Name: FoldEqTrue
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldEqTrue replaces x = True with x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldEqTrue`, not the other rules in that file):

```
# FoldEqTrue replaces x = True with x.
[FoldEqTrue, Normalize]
(Eq $left:* (True))
=>
$left
```
