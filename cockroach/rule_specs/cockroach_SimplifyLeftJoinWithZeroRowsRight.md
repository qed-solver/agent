# Name: SimplifyLeftJoinWithZeroRowsRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

SimplifyLeftJoinWithZeroRowsRight replaces a LeftJoin with a Project when
the right input never returns any rows. The Project passes through columns
from the left input and produces NULL values for each column in the right
input.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyLeftJoinWithZeroRowsRight`, not the other rules in that file):

```
# SimplifyLeftJoinWithZeroRowsRight replaces a LeftJoin with a Project when
# the right input never returns any rows. The Project passes through columns
# from the left input and produces NULL values for each column in the right
# input.
[SimplifyLeftJoinWithZeroRowsRight, Normalize]
(LeftJoin $left:* $right:* & (HasZeroRows $right))
=>
(Project $left (MakeNullProjections $right) (OutputCols $left))
```
