# FoldPlusZero

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

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
```

## Independent verifier review

**Verdict:** AGREE

FoldPlusZero's correctness rests entirely on the additive identity (x + 0 = x) and cast semantics, but QED's fixed oracle theory models scalar operators and casts only as uninterpreted function symbols with no axioms — even encoding the pattern with Calcite's real PLUS/CAST SqlOperators would serialize to UFs the prover has no theory for (the only interpreted addition is internal nat-addition, not user-facing). No re-encoding can bridge that gap: `plus(x, 0)` and `cast(x)` have different operand structures so they cannot be collapsed into one shared symbol, and smuggling the identity in as a table "guaranteed" constraint would merely assume the rule as a premise, making the proof circular rather than universal. Hence a countermodel exists for every encoding, no genuine non-trivial special case is provable (the same reasoning sinks the whole numeric-fold family), and the porter's UNSUPPORTED conclusion is correct.
