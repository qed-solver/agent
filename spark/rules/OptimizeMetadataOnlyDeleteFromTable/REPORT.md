# OptimizeMetadataOnlyDeleteFromTable

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/OptimizeMetadataOnlyDeleteFromTable.scala, lines 39-128
```

## Independent verifier review

**Verdict:** AGREE

All four plan nodes involved (ReplaceData/WriteDelta with a DELETE command before, DeleteFromTableWithFilters/DeleteFromTable after) are side-effecting DML commands with no output row bag, so the rewrite's meaning is an effect-on-storage statement, not a relation transformation — QED's decision procedure is bag equivalence of read-side relational expressions and has no domain for command effects, and an `extend_dsl_file` can't fix that since the fixed Rust prover only interprets query operators. Even re-casting the rule as equivalence of post-delete table states would require proving the engine-specific `translateFilterV2` translation is semantically equivalent to the original condition, which QED cannot do for independent uninterpreted predicates, while the `canDeleteWhere`/`TruncatableTable` capability guards and the no-subquery/translation-success preconditions are inexpressible in an unconditional RuleScript pattern pair; the only QED-provable fragment (the `cond == TrueLiteral` truncate case reducing to `Filter(False, T) ≡ Empty`) is a vacuous tautology that captures none of the rule's actual content.
