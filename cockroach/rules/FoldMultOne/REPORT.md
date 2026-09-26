# FoldMultOne

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

FoldMultOne folds $left * 1 for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldMultOne`, not the other rules in that file):

```
# FoldMultOne folds $left * 1 for numeric types.
[FoldMultOne, Normalize]
(Mult $left:* $right:(Const 1))
=>
(Cast $left (BinaryType Mult $left $right))
```
```

## Independent verifier review

**Verdict:** AGREE

FoldMultOne depends on the numeric algebra identity `x * 1 = cast(x, T)`, but RuleScript/QED can only express `Mult` and `Cast` as uninterpreted scalar functions with no numeric-axiom knowledge, and the DSL cannot faithfully name the constant `1`. Even if a DSL extension introduced a numeric literal, it would lower to an uninterpreted constant in the immutable prover, and the mult-by-one identity would still be unprovable.
