# Name: FoldPlusZero
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/numeric.opt

FoldPlusZero folds $left + 0 for numeric types.

Note: It is necessary to cast $left to the column type of the binary
operation since the type of $left may not match the column type. For example,
1::int + 0::decimal should result in 1::decimal, not 1::int. The execution
engine panics when it expects one type but receives another, so this cast is
essential. If $left is already of the correct type, the cast will be removed
by the EliminateCast rule. Otherwise, if $left is a constant, the cast will
be folded away by the FoldCast rule.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldPlusZero`, not the other rules in that file):

```
# FoldPlusZero folds $left + 0 for numeric types.
#
# Note: It is necessary to cast $left to the column type of the binary
# operation since the type of $left may not match the column type. For example,
# 1::int + 0::decimal should result in 1::decimal, not 1::int. The execution
# engine panics when it expects one type but receives another, so this cast is
# essential. If $left is already of the correct type, the cast will be removed
# by the EliminateCast rule. Otherwise, if $left is a constant, the cast will
# be folded away by the FoldCast rule.
[FoldPlusZero, Normalize]
(Plus $left:* $right:(Const 0))
=>
(Cast $left (BinaryType Plus $left $right))
```
