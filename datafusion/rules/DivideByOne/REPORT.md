# DivideByOne

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1197-1204
```

## Independent verifier review

**Verdict:** AGREE

I verified the porter's two claims against the ground-truth sources: `RexRN.java` exposes only `trueLiteral`/`falseLiteral` (no numeric literal constructor, so the constant "1" cannot even be named faithfully in a pattern), and per the reference's Limitations, QED treats non-boolean scalar operators like `Divide` as uninterpreted function symbols with no axioms connecting them to their arguments — the identity `x / 1 = x` rests precisely on division's internal numeric semantics, which QED "cannot see through" and which no DSL extension can supply, since the prover is the unmodifiable arbiter. No relational/bag-structural re-encoding can avoid the arithmetic identity (the rule is purely a scalar simplification over a single projection), so this is a genuine QED limitation, consistent with the prior FoldDivOne precedent. ```
