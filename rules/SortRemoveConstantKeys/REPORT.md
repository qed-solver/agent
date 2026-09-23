# SortRemoveConstantKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveConstantKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is order-preserving — it drops (or deletes) sort keys whose columns are constant so the emitted row *sequence* is unchanged — but QED's bag model gives Sort no ordering (a Sort is bag-identical to its input), so any before/after pair collapses to the vacuous R = R and certifies nothing about the actual sort order. Compounding this, the rule's precondition (a key column is constant) is read from RelOptPredicateList.constantMap, i.e. it is predicate-inference over the input's pulled-up predicates, which QED explicitly cannot perform between independent uninterpreted symbols. Both are fundamental limitations of the prover's model (not missing DSL builders that extend_dsl_file could fix), so the porter's UNSUPPORTED call is correct. ```
