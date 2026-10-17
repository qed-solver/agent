# UnwrapCastAroundComparison

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/unwrap_cast.rs, lines 1-250
```

## Independent verifier review

**Verdict:** AGREE

The rewrite `cast(x,T) op v ≡ x op cast(v,T)` is valid only under axioms about the concrete cast (injectivity for Eq/NotEq, order-embedding for ordered comparisons, and range-containment so the literal's reverse cast is lossless), but RuleScript/QED models every projection and predicate — casts and comparisons included — as uninterpreted symbols over type-erased integers with no axiom mechanism, so the SMT solver can always build a countermodel (e.g., a constant-valued "cast" plus an arbitrary predicate) and refute the equivalence. The gap is not a missing DSL builder — the shape `filter(op(proj(x), c)) → filter(op(x, proj(c)))` is expressible and would compile — it is the prover's axiom-free semantics for uninterpreted functions, which the Rust QED binary cannot be modified to extend; and the only degenerate escape (identity cast) collapses into a tautology, since type erasure makes "cast to the same type" indistinguishable from no cast, so no genuine narrower special case exists to prove.
