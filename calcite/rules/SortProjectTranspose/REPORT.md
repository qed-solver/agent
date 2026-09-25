# SortProjectTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 12  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortProjectTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortProjectTranspose is an ordering claim end to end — its soundness (and its permutation/monotonic-cast side conditions on sort keys) exists solely to guarantee that `Project(P, Sort(C', X))` yields the same *ordered sequence* as `Sort(C, Project(P, X))` under the remapped collation, whereas QED only decides bag-semantic equivalence and explicitly has no model of Sort/Offset/Limit ordering (rulescript.pdf §7.1; qed.pdf §6.2). At the bag level both sides collapse to the same multiset of P applied to the input regardless of collation, so the best achievable encoding would prove a vacuous tautology and could never express or check the collation↔projection remapping or the side conditions — the `LogicalSort` case in JSONSerializer only shows the JSON can carry a sort node, not that the immutable prover models its ordering semantics, so this is a fundamental QED limitation, not a closable DSL gap. The porter's UNSUPPORTED verdict is therefore correct. ```
