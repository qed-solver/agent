# SortRemoveRedundant

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortRemoveRedundantRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's before-pattern is necessarily a Sort node (ORDER BY, ORDER BY+LIMIT, or pure LIMIT), and its correctness rests on ordered-result preservation, which QED cannot model — per qed.pdf §6.2 it is bag-semantic and has no Sort/Limit/Offset semantics, so the only checkable reading, bag(sort R) = bag R, is a trivial identity true for every R *without* the rule's precondition and would verify nothing of the rule's actual claim; separately, the rule's single soundness precondition — that the input's max row count (optimizer cardinality metadata) is ≤ 1 or ≤ the literal fetch — cannot be stated about an uninterpreted relation, since the DSL exposes no row-count/size constraint (the scan `unique` key flag only forces ≤1 row on a single-column scan, covering neither the general metadata bound nor the fetch-n case, and pure LIMIT is not even a bag identity when |R| > n). No `extend_dsl_file` can close the gap: RelRN has no Sort/Limit builder, and one that merely emits the JSON format's `LogicalSort` encoding would still be judged by the immutable prover, which lacks list semantics — a documented fundamental limitation, not a porter oversight.
