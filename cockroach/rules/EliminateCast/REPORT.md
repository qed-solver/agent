# EliminateCast

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

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
```

## Independent verifier review

**Verdict:** AGREE

EliminateCast's soundness rests entirely on the axiom "a cast to the input's own type is the identity function," but QED's SMT prover treats scalar operators like casts as uninterpreted functions with no cast/identity theory, so it can never establish cast_T(x) = x. Compounding this, every RelType.VarType is flattened to INTEGER in JSONSerializer (VarType.getName() always returns "INTEGER"), so the HasColType type-identity guard — a purely static, value-less check — serializes to no information the prover can consume, and an uninterpreted guard predicate cannot be linked to an uninterpreted cast function. This is a fundamental QED/prover limitation rather than a DSL gap: a dedicated cast operator would still serialize to the same uninterpreted call-with-erased-type JSON form, and closing the gap would require adding a cast-identity theory to the off-limits Rust prover; the porter's empirical provable:false on the faithful Filter(guard,scan)+cast-project vs bare-field encoding is the expected outcome, and no symbol-sharing fix or narrower special case can recover a provable form without dropping the cast itself. ```
