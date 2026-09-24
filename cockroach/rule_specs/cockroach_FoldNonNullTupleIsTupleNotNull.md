# Name: FoldNonNullTupleIsTupleNotNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullTupleIsTupleNotNull replaces x IS NOT NULL with True if x is a
tuple with only constant, non-null elements.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullTupleIsTupleNotNull`, not the other rules in that file):

```
# FoldNonNullTupleIsTupleNotNull replaces x IS NOT NULL with True if x is a
# tuple with only constant, non-null elements.
[FoldNonNullTupleIsTupleNotNull, Normalize]
(IsTupleNotNull $input:(Tuple) & (HasAllNonNullElements $input))
=>
(True)
```
