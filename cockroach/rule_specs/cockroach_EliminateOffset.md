# Name: EliminateOffset
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

EliminateOffset discards an Offset operator if its offset value is zero.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `EliminateOffset`, not the other rules in that file):

```
# EliminateOffset discards an Offset operator if its offset value is zero.
[EliminateOffset, Normalize]
(Offset $input:* (Const 0))
=>
$input
```
