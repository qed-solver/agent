# SortRemoveDuplicateKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveDuplicateKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on ordering semantics — that collation [k1, k2] induces the same row order as [k1] when k1 functionally determines k2 — and QED explicitly does not model Order By/Sort ordering semantics, operating in pure bag semantics (qed.pdf §6.1 limitations). Under bag semantics a Sort with any two collations over the same input is the identical bag, so any RuleScript encoding of before/after would be trivially true for arbitrary collations and verify none of the rule's content; the FD premise itself (mq.determines) is also beyond QED's reasoning (it cannot infer functional dependencies, and RelRN exposes no sort operator whose ordering the prover could compare). Adding a Sort builder via extend_dsl_file could not close this, since the gap is in the prover's semantics (which must not be modified), not in the DSL — making UNSUPPORTED the correct conclusion. ```
