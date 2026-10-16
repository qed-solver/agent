# DivideByOne

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1197-1204
```

## Independent verifier review

**Verdict:** AGREE

DivideByOne (A / 1 → A, and the multiply-by-zero variant actually in the given snippet) rests entirely on a scalar arithmetic identity — x / 1 = x (or x * 0 = 0) — but QED's fixed first-order theory contains only equality, a total order, natural-number addition, and ite; it has no division or multiplication algebra. So `Div`/`Mult` (and any `Cast`) can only be introduced as uninterpreted function symbols via `genericProjectionOp`, making the identity non-valid for every instantiation — a gap in the immutable Rust prover's theory that neither a `extend_dsl_file` Java/JSON edit nor any narrower special case can close, consistent with the AGREE SKIPPED verdicts for the structurally-identical FoldDivOne/FoldMultOne. ```
