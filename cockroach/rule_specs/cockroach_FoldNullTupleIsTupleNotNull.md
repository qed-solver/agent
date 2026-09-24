# Name: FoldNullTupleIsTupleNotNull
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullTupleIsTupleNotNull replaces x IS NOT NULL with False if x is a tuple
with at least one constant, null element.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullTupleIsTupleNotNull`, not the other rules in that file):

```
# FoldNullTupleIsTupleNotNull replaces x IS NOT NULL with False if x is a tuple
# with at least one constant, null element.
[FoldNullTupleIsTupleNotNull, Normalize]
(IsTupleNotNull $input:(Tuple) & (HasNullElement $input))
=>
(False)
```
