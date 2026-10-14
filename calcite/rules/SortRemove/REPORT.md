# SortRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortRemoveRule is sound only under the non-bag premise that the child already emits rows in the Sort's collation (a planner trait fact), and QED decides only bag equivalence with no ordering semantics for Sort/Order By (qed.pdf §6.2), so the premise can neither be stated nor the redundancy checked. RuleScript's RRule has no guard/side-condition mechanism on uninterpreted children, and no bag property (e.g. key uniqueness on a scan) implies "already sorted", so no bag-semantic special case exists. Extending the DSL cannot close the gap because the missing ordering interpretation lives in the (unmodifiable) prover, not in the Java builders. ```
