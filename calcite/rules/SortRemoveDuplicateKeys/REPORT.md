# SortRemoveDuplicateKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveDuplicateKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

Both plans sort the same input, so their row bags are identical by construction; the rule's only non-trivial claim is that dropping a key `mq.determines` marks as functionally determined by earlier keys preserves the same row *order*, and QED has no ordering semantics for Sort (it reduces to its input bag-wise), so any encoding proves only a vacuous tautology. The `mq.determines` precondition is a content-dependent property the prover cannot even constrain, since proofs must hold for all instantiations of the uninterpreted table symbols. This is a fundamental QED limitation, not a DSL gap: the JSON serializer already carries `sort`, so a RelRN sort builder (via extend_dsl_file) would only enable vacuous proofs rather than close the missing ordering model, matching the verified AGREE skips for calcite SortRemove and datafusion DedupeSortExprs. ```
