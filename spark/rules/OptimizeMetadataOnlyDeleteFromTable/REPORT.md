# OptimizeMetadataOnlyDeleteFromTable

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/OptimizeMetadataOnlyDeleteFromTable.scala, lines 39-128
```

## Independent verifier review

**Verdict:** AGREE

Both sides of the rewrite are DML statements (row-level ReplaceData/WriteDelta DELETE → metadata-only DeleteFromTableWithFilters/DeleteFromTable), and RuleScript's core language plus the fixed prover's JSON format expose only SELECT operators (scan/filter/project/join/set-ops/aggregate), so no faithful before()/after() pair can be constructed, and an `extend_dsl_file` addition of a delete node is infeasible because the unmodifiable Rust prover cannot interpret a new DML node type. Even a hypothetical uninterpreted Delete symbol would not help: the rewrite's validity rests on the connector-specific `translateFilterV2` conversion being meaning-preserving and on the `canDeleteWhere`/`SupportsDeleteV2`/`TruncatableTable` side conditions, i.e. predicate entailment between independent uninterpreted symbols and a backend operator's bespoke internal semantics — precisely the limitations QED is designed not to cross.
