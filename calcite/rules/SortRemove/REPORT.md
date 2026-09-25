# SortRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortRemoveRule is a physical-order (collation trait) rule, not a relational equivalence: its only precondition — the input's row order already satisfies the sort's collation — has no counterpart in QED's bag-semantic model, where no pattern can express ordering properties of a subplan, and the entire soundness content of the rule is that ordering guarantee. The porter's probe against the real prover was also the correct minimal test: an unconditional single-scan `Sort(R) → R` (one shared symbol, no predicates, so no name-mismatch or composition bug was possible) returned a genuine `provable: false` with no panic or timeout, confirming the limitation sits in the prover's treatment of ordering-semantic operators — something no further DSL extension can work around since the QED prover itself may not be modified — and the only remaining expressible special case (e.g. empty input) is vacuous, so no non-trivial encoding of this rule can exist. ```
