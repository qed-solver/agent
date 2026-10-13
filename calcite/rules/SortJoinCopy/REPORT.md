# SortJoinCopy

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinCopyRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortJoinCopy's only semantic content is ordering — it copies the Sort (without offset/fetch) below each join input purely as a physical hint while leaving the outer Sort unchanged, so under QED's bag semantics, which explicitly do not model Sort/collation/limit/offset, both sides collapse to the identical join bag and the only expressible statement is the vacuous tautology Join(L,R) ≡ Join(L,R). That is a fundamental QED limitation on order semantics, not a missing DSL builder (a `sort()` builder via `extend_dsl_file` would still reduce to bag identity and prove nothing about the rule), consistent with the prior UNSUPPORTED verdict on the sibling rule SortJoinTranspose.
