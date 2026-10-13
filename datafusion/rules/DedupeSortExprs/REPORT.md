# DedupeSortExprs

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_duplicated_expr.rs, lines 68-114
```

## Independent verifier review

**Verdict:** AGREE

The rule only trims the ORDER BY key list while the input (and hence the bag of emitted rows) is unchanged on both sides, so its entire soundness claim — that the deduplicated/FD-pruned collation induces the same row order — is a list/ordering property that QED does not model (qed.pdf §6.2; its evaluation's "List semantic" failure category). The missing Sort builder in RelRN is not the decisive gap: JSONSerializer already emits `sort` nodes into QED's JSON, meaning the prover (the unmodifiable arbiter) simply has no ordering semantics for them, so any Sort/Sort pattern would reduce to two trivially bag-equal plans and yield a vacuous "provable" that verifies nothing. Encoding the lexicographic preorder as a surrogate uninterpreted relation would only prove a separate combinatorial lemma, not an equivalence between plan patterns, so no genuine RuleScript encoding of this rule exists.
