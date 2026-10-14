# SortJoinCopy

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortJoinCopyRule.java
```

## Independent verifier review

**Verdict:** AGREE

SortJoinCopyRule's entire contract is row-ordering: it decomposes the top Sort's collation across the join's two sides, consults "already sorted" metadata, and is only safe because the outer Sort is preserved to re-establish global order. QED models no ordering at all — Sort is a bag identity — so both sides erase to the identical Join bag and the only provable statement is the tautology Join ≡ Join; tellingly, QED would "prove" an unsound variant that drops the outer sort just as readily, so its verdict carries no information about this rule's real correctness. This is exactly the reference's explicit fundamental limitation (correctness fundamentally rests on Sort/row-ordering semantics), not a symbol-sharing bug or a mis-modeling a genuine attempt could fix. ```
