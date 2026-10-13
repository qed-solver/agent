# SortMerge

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortMergeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is ordering/top-N composition — folding `Sort(fetch=t)` over `Sort(fetch=b)` into a single `Sort(fetch=min(t,b))` on the child's collation — and QED decides bag-semantic equivalence only: it has no model of Sort/Limit/Offset, and top-n under arbitrary (empty-collation) tie-breaking is not even a well-defined bag operation, so the identity is unprovable in principle rather than merely unencoded. Even the narrowest special case (two pure LIMITs with no order-by) reduces only to a subset/cardinality relationship, not a bag equality, and the min(t,b) fetch fold is a numeric relation between literals that the uninterpreted-symbol pattern language cannot express. Extending the DSL with a Sort builder would not help, since the immutable QED prover has no ordering semantics to check such a symbol against.
