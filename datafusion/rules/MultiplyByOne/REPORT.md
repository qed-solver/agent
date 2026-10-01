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

The rewrite A * 1 ⟹ A is valid only because 1 is the multiplicative identity of real arithmetic, but QED models scalar operators like Multiply as uninterpreted symbols with no axioms tying them to their arguments (its theory covers only equality/uninterpreted functions, a total order, and natural-number addition), so the identity rests on numeric semantics the fixed prover cannot see through — and `RexRN` (verified in source) offers no numeric-literal constructor, only `trueLiteral`/`falseLiteral`, to express the constant `1` at all. Since the missing piece lives in the immutable prover's theory rather than in a DSL builder, `extend_dsl_file` cannot close the gap: even embedding the expression in a `Project` over a scan, `f(a, 1) = a` is unprovable for any uninterpreted `f`, and no faithful narrower special case of this rule exists — the same fundamental limitation as the verifier-agreed DivideByOne skip. ```
