# CaseFirstBranchAlwaysTrue

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
```

## Independent verifier review

**Verdict:** AGREE

CASE/then-else is not among QED's interpreted scalar operators (its `ite` is only the internal 3VL encoding of And/Or/In/Some, and the JSON format can at best carry a CASE call as a named uninterpreted operator), so in every branch of this rule the before-side pattern contains a literal CASE that QED sees only as an uninterpreted function and can never equate with a then/else branch (e.g. `case(false, A, B) ≡ B` or dead-branch elimination). A relational Filter/Union re-encoding of the CASE value column is not faithful under 3-valued logic (a `c`/`¬c` filter pair silently drops rows where `c` is NULL, and "is not true" would be an independent symbol QED can't relate to `c`), and `extend_dsl_file` only touches the Java builder side — the interpretation lives in the fixed Rust prover — so this is a genuine prover limitation in the "operator whose specific internal semantics QED cannot see through" class, and no narrower special case of the rule escapes it because the before side always contains the unnameable CASE. ```
