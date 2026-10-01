# CaseFirstBranchAlwaysTrue

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
```

## Independent verifier review

**Verdict:** AGREE

This rule's correctness rests entirely on the CASE operator's 3-valued branch-selection semantics (e.g. `case(TRUE, a, b) ≡ a`, dropping `WHEN false` branches), and QED's interpreted fragment contains no CASE/ite — any CASE encoding serializes to an uninterpreted scalar call, for which the equality is not valid under universal instantiation, so no SMT proof exists. Every instance of the rule, including the narrowest special cases (`CASE WHEN true THEN a END → a`, `CASE WHEN false THEN a ELSE b END → b`), necessarily contains CASE on the before side, so no special case escapes the uninterpreted-operator gap. This is a fixed-prover semantics limitation (the interpretation is keyed by operator name inside the Rust prover), which `extend_dsl_file` cannot address since it only touches the Java builders/serializer. ```
