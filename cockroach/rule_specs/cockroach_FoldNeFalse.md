# Name: FoldNeFalse
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNeFalse replaces x != False with x.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNeFalse`, not the other rules in that file):

```
# FoldNeFalse replaces x != False with x.
[FoldNeFalse, Normalize]
(Ne $left:* (False))
=>
$left
```
