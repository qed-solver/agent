# Name: FoldNullTupleIsTupleNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullTupleIsTupleNull replaces x IS NULL with True if x is a tuple with
only constant, null elements.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullTupleIsTupleNull`, not the other rules in that file):

```
# FoldNullTupleIsTupleNull replaces x IS NULL with True if x is a tuple with
# only constant, null elements.
[FoldNullTupleIsTupleNull, Normalize]
(IsTupleNull $input:(Tuple) & (HasAllNullElements $input))
=>
(True)
```
