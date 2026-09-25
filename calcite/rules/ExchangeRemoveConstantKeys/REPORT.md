# ExchangeRemoveConstantKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ExchangeRemoveConstantKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule only rewrites the physical properties of Exchange/SortExchange (hash-distribution keys and collation fields), leaving the bag of rows untouched, and the core language exposes no Exchange/SortExchange — or even Sort — operator at all. More fundamentally, QED models only bag semantics with no notion of distribution or row ordering, so the rule's actual content (constant keys degenerating hash placement to SINGLETON, constant collation fields being vacuous) is inexpressible, and its precondition (the constantMap from pulled-up predicates) is a metadata inference QED cannot perform between uninterpreted symbols. Extending the DSL couldn't help, since the gap lies in QED's model itself (the unmodifiable arbiter has no distribution/ordering semantics to check against), so any before/after encoding collapses to the vacuous R = R, confirming the porter's UNSUPPORTED call.
