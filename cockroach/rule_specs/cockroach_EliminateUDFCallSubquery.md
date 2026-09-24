# Name: EliminateUDFCallSubquery
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateUDFCallSubquery replaces a subquery with a udf call if the subquery's
input is a single-row, single-column Values expression with a udf call.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateUDFCallSubquery`, not the other rules in that file):

```
# EliminateUDFCallSubquery replaces a subquery with a udf call if the subquery's
# input is a single-row, single-column Values expression with a udf call.
[EliminateUDFCallSubquery, Normalize]
(Subquery (Values [ (Tuple [ $udf:(UDFCall) ]) ]))
=>
$udf
```
