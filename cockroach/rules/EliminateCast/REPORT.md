# EliminateCast

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

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

EliminateCast's soundness rests entirely on the cast operator's own value semantics — that a cast is value-identical to its input when the input type equals the target type — guarded by the type-level side condition `HasColType`, which the pattern language cannot express (type names are inert labels, all lowered to integers in the prover, and no predicate can assert type equality between symbols). Any faithful encoding therefore reduces to proving `cast(x) ≡ x` for an uninterpreted scalar function, which QED's SMT translation cannot justify; adding a cast builder via `extend_dsl_file` would not help because the unmodifiable prover interprets all scalar functions as uninterpreted regardless. The blocker is on the trusted-prover side, so no faithful, non-vacuous encoding exists — the only "provable" version (dropping the cast entirely) is the vacuous tautology, not the rule. ```
