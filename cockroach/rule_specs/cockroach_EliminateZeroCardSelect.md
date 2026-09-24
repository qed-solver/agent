# Name: EliminateZeroCardSelect
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

EliminateZeroCardSelect eliminates a Select when its input has zero
cardinality. The filter expressions are per-row and will never be evaluated,
so it is safe to remove them regardless of volatility. This complements the
SimplifyZeroCardinalityGroup rule which requires the entire expression to be
leakproof.

Extracted from `select.opt` (which defines multiple rules — implement specifically `EliminateZeroCardSelect`, not the other rules in that file):

```
# EliminateZeroCardSelect eliminates a Select when its input has zero
# cardinality. The filter expressions are per-row and will never be evaluated,
# so it is safe to remove them regardless of volatility. This complements the
# SimplifyZeroCardinalityGroup rule which requires the entire expression to be
# leakproof.
[EliminateZeroCardSelect, Normalize]
(Select $input:* & (HasZeroRows $input) $filters:*)
=>
$input
```
