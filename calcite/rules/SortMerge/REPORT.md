# SortMerge

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortMergeRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortMerge's soundness is purely a top-N/row-position statement — it must know which rows each LIMIT keeps and, even in the degenerate sub-case where the outer fetch ≥ inner fetch (top sort is a no-op), that the inner `Limit B` node produces at most B rows — and QED's theory is bag-semantics only, explicitly without any model for the ordering semantics of Sort/Limit/Offset (qed.pdf §6.2), so the SMT solver has no algebraic relation to bridge. The gap is in the prover's semantics rather than a missing DSL builder (RelRN exposes no sort/limit and the Rust prover, the unchanging arbiter, has no ordering theory), so `extend_dsl_file` cannot close it; a "proof" obtained by erasing sorts to identity would only show identity = identity and would verify nothing about which rows survive, and no genuinely non-trivial special case of the min-of-fetches law exists within bag semantics.
