# Name: CommuteNullIs
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

CommuteNullIs moves a NULL onto the right side of an IS/IS NOT comparison.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CommuteNullIs`, not the other rules in that file):

```
# CommuteNullIs moves a NULL onto the right side of an IS/IS NOT comparison.
[CommuteNullIs, Normalize]
(Is | IsNot $left:(Null) $right:^(Null))
=>
((OpName) $right $left)
```
