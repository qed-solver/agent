# ExchangeRemoveConstantKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ExchangeRemoveConstantKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

ExchangeRemoveConstantKeysRule only rewrites the physical properties of an Exchange/SortExchange (hash-distribution keys and collation fields); under bag semantics both sides of the rewrite are literally the same relation, and the rule's actual correctness argument — that a known-constant key makes hash distribution degenerate to singleton placement and a constant collation field vacuous — is a claim about node placement and row ordering, exactly the physical semantics QED does not model (same category as its unmodeled Sort/Window/list semantics). No faithful encoding exists: the DSL has no Exchange operator and none can be added meaningfully (an identity model would only vacuously prove the *general* removal Calcite deliberately does not do, without ever using the constant-key premise; an uninterpreted-relational model would leave before/after as distinct symbols QED cannot equate), so this is a genuine QED limitation, not a missing-builder gap — the porter's context-overflow error notwithstanding, a real attempt could at best produce a trivial X≡X proof with no content.
