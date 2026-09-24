# ProjectWindowTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 12  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectWindowTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on Window semantics (one output row per input row, partition/order/frame-dependent values), but QED — the unmodifiable prover — has no window model and its JSON input language has no Window node (JSONSerializer's switch has no LogicalWindow case), so no faithful encoding exists. The only apparent special case (partition-wide aggregation via join+group-by) also fails: reconstructing per-row aggregate values requires an equi-join, but the DSL only offers uninterpreted join conditions with no equality operator, so the two sides' joins cannot be related. Substituting an Aggregate would collapse each partition to one row (wrong bag semantics) and prove only an unrelated column-pruning fact, and treating the window as an opaque scan discards the very relation the rule establishes (that the recomputed window over trimmed input matches the original).
