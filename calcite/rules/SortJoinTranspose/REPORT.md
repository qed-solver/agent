# SortJoinTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness is fundamentally an ordering/top-N argument: replacing the join input L with just its first (offset+fetch) rows under the sort collation is only justified because every row of the final top (offset+fetch) window must originate from one of those rows. QED's decision procedure is universal bag equivalence and gives Sort/Limit/Offset no bag-semantic meaning, so the before side (Join(L, R)) and the after side (Join(topK(L), R)) are not bag-equal in general (each L row outside the top K drops its join-partner copies from the bag), and no special case can keep the row-dropping that makes the rule useful while staying bag-equivalent. Extending the DSL with a Sort builder would not close the gap either: the prover cannot reason about top-N semantics at all, and any bag-identity treatment of sort would reduce both sides to the identical join, yielding only a vacuous proof.
