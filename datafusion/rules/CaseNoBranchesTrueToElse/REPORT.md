# CaseNoBranchesTrueToElse

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
```

## Independent verifier review

**Verdict:** AGREE

This rule is pure scalar CASE algebra — every branch of it (true-branch short-circuit, dropping false branches, empty-CASE → else-expr/NULL) rests on the selection semantics of the CASE operator, but QED's surface fragment has no interpreted ite/CASE: scalar functions serialize as plain uninterpreted RexCalls and QED quantifies over *all* instantiations of uninterpreted symbols, so it can never equate case(p,a,b) with a or b (the universal statement is false for arbitrary function symbols). A `Case` builder via extend_dsl_file would only mint another uninterpreted operator name for the fixed Rust prover, and relational workarounds (e.g. modeling ite as a union of filtered projections) verify a self-chosen model against itself, not the backend's actual CASE operator — a genuine QED limitation, not a missed encoding. ```
