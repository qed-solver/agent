# ExchangeRemoveConstantKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ExchangeRemoveConstantKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire correctness content is physical placement — hashing on a constant-valued key puts every row in one bucket, so that key (or the whole Exchange, reduced to SINGLETON) can be dropped — and QED decides equivalence purely under bag semantics, in which row placement across nodes is invisible: before and after yield the identical bag for any input, and no axiom relating hash(K) to hash(K∖constants) or to the identity exists or could be expressed in the prover's fragment. The Exchange operator is also absent from both the DSL and the serializer/prover (an uninterpreted, axiom-free symbol if added — the same barrier confirmed for the Sort precedents EliminateSorts and CapSortFetchWithLimit), and stripping the physical semantics leaves only the trivial identity rewrite, so no genuine non-trivial provable special case remains. ```
