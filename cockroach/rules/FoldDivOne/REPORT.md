# FoldDivOne

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

FoldDivOne folds $left / 1 for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldDivOne`, not the other rules in that file):

```
# FoldDivOne folds $left / 1 for numeric types.
[FoldDivOne, Normalize]
(Div $left:* $right:(Const 1))
=>
(Cast $left (BinaryType (OpName) $left $right))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on the scalar identity x / 1 = Cast(x, τ), but QED's fixed theory (equality, uninterpreted functions/predicates, a total order per sort, natural-number addition, ite) contains no division algebra and models casts only as uninterpreted functions, so Div(x,1) and Cast(x) are distinct opaque terms with no logical validity relating them under any bag-semantic encoding. Since the Rust prover is the immutable arbiter, no DSL extension can supply the missing axioms — and the incidental gap that RexRN.Const hardcodes the literal 0 (verified in source) is moot once division/cast semantics are absent anyway. No faithful narrower special case survives either: even the cast-free variant x/1 = x still requires division semantics QED does not model, since a single-column projection only proves equality when its target expressions are pointwise equal. ```
