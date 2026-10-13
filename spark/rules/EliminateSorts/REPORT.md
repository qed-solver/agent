# EliminateSorts

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2430-2502
```

## Independent verifier review

**Verdict:** AGREE

Every case of EliminateSorts rewrites a `Sort` node itself (dropping it or trimming its keys), so any before-pattern must contain a Sort, and the rule's soundness rests on ordering facts — empty/constant-only keys make a sort a no-op, join output has no defined order, and aggregation destroys input order — none of which QED's bag-semantic model captures; qed.pdf's own failure analysis (§6.2 "List semantics") states QED does not support ordering semantics of Sort/Limit/Offset, so a Sort node would be an uninterpreted, axiom-free operator and no equation like `Sort(x) ≡ x` or `Join(Sort(l), Sort(r)) ≡ Join(l, r)` is derivable. Even the degenerate "Sort with zero keys → child" case is unprovable, since it *is* the bag-identity claim for an operator the prover has no interpretation of, so adding a `RelRN.sort` builder (the JSON format can carry sort nodes, but the prover can't) would close no gap. This is consistent with the AGREE-d EliminateLimits/EliminateOffsets Spark precedents, where the fixed prover's lack of a bag interpretation for the operator in question was likewise confirmed as the fundamental barrier.
