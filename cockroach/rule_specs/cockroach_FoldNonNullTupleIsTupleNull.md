# Name: FoldNonNullTupleIsTupleNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullTupleIsTupleNull replaces x IS NULL with False if x is a tuple
with at least one constant, non-null element.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullTupleIsTupleNull`, not the other rules in that file):

```
# FoldNonNullTupleIsTupleNull replaces x IS NULL with False if x is a tuple
# with at least one constant, non-null element.
[FoldNonNullTupleIsTupleNull, Normalize]
(IsTupleNull $input:(Tuple) & (HasNonNullElement $input))
=>
(False)
```
