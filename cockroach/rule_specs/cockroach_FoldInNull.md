# Name: FoldInNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

FoldInNull replaces the In/Not operator with Null when the tuple only
contains null. The NormalizeInConst pattern will reduce multiple nulls to a
single null when it removes duplicates, so this pattern will match that.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `FoldInNull`, not the other rules in that file):

```
# FoldInNull replaces the In/Not operator with Null when the tuple only
# contains null. The NormalizeInConst pattern will reduce multiple nulls to a
# single null when it removes duplicates, so this pattern will match that.
[FoldInNull, Normalize]
(In | NotIn $left:* (Tuple [ (Null) ]))
=>
(Null (BoolType))
```
