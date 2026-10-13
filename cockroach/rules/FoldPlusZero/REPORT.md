# FoldPlusZero

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

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

FoldPlusZero's soundness rests on the numeric identity x + 0 = x together with the cast-to-binary-type being a no-op on a correctly typed operand, but QED encodes Plus and Cast as uninterpreted function symbols over uninterpreted types, exposes no numeric zero literal (only boolean literals), and has no arithmetic axioms linking distinct operators — so the before and after expressions are not equal under arbitrary instantiation of those symbols, which is exactly the "operator's bespoke internal semantics" limitation. This is not a closable DSL gap: even a `extend_dsl_file` addition of a zero-constant builder would yield merely an uninterpreted constant in the prover, which still lacks any axiom equating plus(x, 0) with cast(x), and the QED prover itself is off-limits. The conclusion matches the already-verified sibling rules FoldMinusZero and FoldDivOne from the same numeric.opt file, which fail for the identical fundamental reason. ```
