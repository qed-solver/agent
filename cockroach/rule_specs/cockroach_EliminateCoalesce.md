# Name: EliminateCoalesce
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateCoalesce discards the Coalesce operator if it has a single operand.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateCoalesce`, not the other rules in that file):

```
# EliminateCoalesce discards the Coalesce operator if it has a single operand.
[EliminateCoalesce, Normalize]
(Coalesce [ $item:* ])
=>
$item
```
