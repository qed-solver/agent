# BitwiseXorByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 12  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1376-1381
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on the scalar identity x ^ 0 = x, but QED encodes non-boolean operators (including `^`) as uninterpreted function symbols with no numeric-zero constant in its theory (only boolean literals exist in the Rex language) and no axioms relating operators to constants, so any faithful encoding has an SMT countermodel (e.g. interpret `^` as a function that need not return its first argument on zero). This is a limitation of the prover's theory rather than a DSL gap — `extend_dsl_file` can only add builders that serialize to uninterpreted symbols and cannot add arithmetic axioms to the fixed prover. It is consistent with the independently verified skip verdicts for the sibling `BitwiseAndByZero` and the `FoldPlusZero`/`FoldMinusZero`/`FoldDivOne` arithmetic-folding family, which rest on the same missing operator theory. ```
