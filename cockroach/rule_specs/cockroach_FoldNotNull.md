# Name: FoldNotNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

FoldNotNull replaces NOT(Null) with Null.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `FoldNotNull`, not the other rules in that file):

```
# FoldNotNull replaces NOT(Null) with Null.
[FoldNotNull, Normalize]
(Not (Null))
=>
(Null (BoolType))
```
