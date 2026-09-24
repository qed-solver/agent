# Name: EliminateCast
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateCast discards a cast if its input already has a type that's identical
to the desired static type.

Note that CastExpr removes unnecessary casts during type-checking; this rule
can still be helpful if some other rule creates an unnecessary CastExpr.

EliminateCast is marked as high-priority so that it matches before FoldCast.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateCast`, not the other rules in that file):

```
# EliminateCast discards a cast if its input already has a type that's identical
# to the desired static type.
#
# Note that CastExpr removes unnecessary casts during type-checking; this rule
# can still be helpful if some other rule creates an unnecessary CastExpr.
#
# EliminateCast is marked as high-priority so that it matches before FoldCast.
[EliminateCast, Normalize, HighPriority]
(Cast $input:* $targetTyp:* & (HasColType $input $targetTyp))
=>
$input
```
