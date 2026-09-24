# Name: EliminateMax1Row
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/max1row.opt

EliminateMax1Row discards the Max1Row operator if its input is statically
guaranteed to have no more than one row. Removing the Max1Row operator is
important when decorrelating subqueries, as it interferes with ApplyJoin
pushdown when it's present.

Extracted from `max1row.opt` (which defines multiple rules — implement specifically `EliminateMax1Row`, not the other rules in that file):

```
# EliminateMax1Row discards the Max1Row operator if its input is statically
# guaranteed to have no more than one row. Removing the Max1Row operator is
# important when decorrelating subqueries, as it interferes with ApplyJoin
# pushdown when it's present.
[EliminateMax1Row, Normalize]
(Max1Row $input:* & (HasZeroOrOneRow $input))
=>
$input
```
