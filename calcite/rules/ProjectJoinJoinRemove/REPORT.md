# ProjectJoinJoinRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 105  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's row-level validity hinges on B's key being unique AND the top join condition referencing only the shared A-key column plus C (never B); proving that as a *bag* identity over a two-level nullable LEFT-join stack with a column-skipping Project sits outside QED's complete bag-semantic fragment (complete_fragment: false, rejected structurally in ~1–2 µs before SMT across four encodings that varied equi-vs-uninterpreted condition, B unique/non-unique, and projection shape), while the structurally identical stack under an Aggregate proves — so the wall is QED's own incomplete fragment for a bag Project atop a multi-level LEFT-join stack, not a DSL-expressibility gap or an uncorrected symbol mismatch (a name mismatch would instead yield an authoritative complete_fragment:true "not provable").
