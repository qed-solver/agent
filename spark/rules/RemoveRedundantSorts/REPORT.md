# RemoveRedundantSorts

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RemoveRedundantSorts.scala, lines 29-62
```

## Independent verifier review

**Verdict:** AGREE

The rule's two substantive rewrites are justified solely by non-bag facts: the child's output-ordering property already satisfying the sort keys, and the global-sort-under-local-sort demotion existing to preserve *partitioning* via RepartitionByExpression, not ordering. QED's model is bag-only and explicitly does not support the ordering semantics of Sort/Order By (qed.pdf §6.2), and RuleScript has no way to express "child is already sorted by X" as a side condition on an uninterpreted child — nor any partitioning concept — and even if a `sort` builder were added to the DSL (JSONSerializer already carries LogicalSort), the unmodifiable Rust prover has no ordering/partitioning semantics to check against, so the only provable statement would be the vacuous bag identity `sort(child) ≡ child`, which is not the rule being ported. Thus the porter correctly identified a genuine QED limitation rather than a missing DSL capability, and UNSUPPORTED is the right conclusion. ```
