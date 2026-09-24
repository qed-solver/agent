# Name: EliminateConstValueSubquery
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateConstValueSubquery replaces a subquery with a constant value if the
subquery's input is a single-row, single-column Values expression with a
constant value.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateConstValueSubquery`, not the other rules in that file):

```
# EliminateConstValueSubquery replaces a subquery with a constant value if the
# subquery's input is a single-row, single-column Values expression with a
# constant value.
[EliminateConstValueSubquery, Normalize]
(Subquery (Values [ (Tuple [ $value:(Const) ]) ]))
=>
$value
```
