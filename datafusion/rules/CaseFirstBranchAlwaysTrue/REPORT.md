# CaseFirstBranchAlwaysTrue

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness rests entirely on CASE's branch-selection semantics, and any CASE node appearing in the before pattern is seen by QED only as a name-keyed uninterpreted function — its interpreted scalar fragment is just AND/OR/NOT/TRUE/FALSE — so the claimed equivalence reduces to "uninterpreted f(x,a,y,b,q) ≡ (x∧a) ∨ (y∧¬x∧b) ∨ (¬(x∨y)∧q)", which a single instantiation falsifies (all argument predicates true, f false) and no `extend_dsl_file` change can fix, since operator interpretation is keyed by name inside the immutable Rust prover. This is precisely the "operator's bespoke internal semantics QED can't see through" limitation, consistent with the AGREE-verified SKIPPED precedent SimplifyCaseSingleTruthyBranch from the same rule family. No narrower special case is salvageable either, because any rule that eliminates a CASE node requires the prover to interpret CASE itself. ```
