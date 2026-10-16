# FoldMultOne

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

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

FoldMultOne's correctness rests entirely on the scalar identity x * 1 = cast_τ(x), but QED's fixed theory contains no multiplication axioms (its only built-in arithmetic is natural-number addition) and, per its own evaluation, interprets explicit type casts only as uninterpreted functions. In RuleScript, `Mult` and `Cast` can therefore only be introduced as distinct uninterpreted operator symbols (via `genericProjectionOp`), so `Mult(x, 1) = Cast(x)` is not a logical validity for any instantiation — a countermodel exists whenever the value domain is non-empty, and narrowing to constant operands or unique/empty sources cannot help since the two symbols are opaque on every value. The missing axioms would have to be added inside the Rust prover itself, which is the immutable arbiter, so no DSL extension or special case can recover this — consistent with the already-verified AGREE verdicts for the sibling rules FoldDivOne and FoldCast. ```
