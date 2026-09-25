# SortUnionTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortUnionTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortUnionTranspose's soundness is exactly the top-N order property that the first (offset+fetch) rows of each branch under the sort collation suffice for the final top-(offset,fetch) of the merged result — an ordering/top-k fact that QED's bag-semantic, uninterpreted-domain calculus has no way to express (qed.pdf lists list/ordering semantics as explicitly unsupported, and Sort/Offset/Fetch have no bag-semantic meaning). The DSL indeed exposes no Sort builder, and that is not a closable DSL gap: even a `sort` builder emitting JSONSerializer's existing LogicalSort form would hit a limitation on the prover side itself (the unmodifiable trusted arbiter has no ordering semantics), so at best both sides would collapse to the same bag and prove a vacuous identity that verifies nothing about which rows survive the push-down. ```
