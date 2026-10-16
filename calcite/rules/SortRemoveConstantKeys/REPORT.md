# SortRemoveConstantKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveConstantKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule is sound only because dropping a sort key that the input's predicates prove constant preserves the output row *ordering* — and QED decides only bag equivalence, under which `Sort`/`Limit`/`Offset` carry no semantics at all, so both branches (partial key-dropping and full removal) reduce to bag tautologies like `R ≡ R` for any key set (e.g. `Sort([f1,f2], Filter(f1=c, R))` and `Sort([f2], Filter(f1=c, R))` are equal as bags even when f1 is *not* constant). `RelRN` exposes no order-carrying operator, and even if a `Sort` builder were added via `extend_dsl_file` (the serializer can carry collations, but the SMT encoding has no ordering model), the prover could only check the vacuous bag identity, not the order-preservation argument that constitutes the rule's entire correctness content. ```
