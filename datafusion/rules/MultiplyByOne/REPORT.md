# MultiplyByOne

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1152-1159
```

## Independent verifier review

**Verdict:** AGREE

The rewrite `A * 1 → A` (and `1 * A → A`) is valid only under the multiplicative-identity axiom for actual arithmetic multiplication; QED's fixed theory (equality, total order, addition, ite) contains no multiplication, so any encoding must treat `*` as an uninterpreted symbol, and `f(A,1)=A` is refutable by a countermodel rather than universally provable — the same "operator-specific internal semantics QED cannot see through" limitation. The missing literal `1` in `RexRN.Const` is a DSL gap that `extend_dsl_file` could in principle close, but that is moot: the missing arithmetic axiom lives in the immutable Rust prover, which is the hard boundary no DSL extension can cross. This matches the already-verified SKIPPED precedents for the structurally identical rules FoldOneMult (cockroach) and DivideByOne (datafusion). ```
