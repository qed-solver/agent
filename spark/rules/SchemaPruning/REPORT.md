# SchemaPruning

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 46  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/SchemaPruning.scala, lines 38-248
```

## Independent verifier review

**Verdict:** AGREE

The rule's before- and after-plans scan two distinct leaf relations (the full-schema HadoopFsRelation and its copy with a pruned data/metadata schema), and the rewrite is sound only under the physical file-format guarantee that a pruned Parquet/ORC read returns identical values for the kept fields — a cross-table premise that QED's for-all-instantiations equivalence problem cannot carry (its per-table "guaranteed" constraints are single-table and not exposed by the DSL at all), so with two independent uninterpreted scans the equivalence is refutable, not provable. The only encoding that could be "proven" — modeling the pruned scan as a projection over the same scan symbol — would bake the very data-source property under verification into the after-plan, leaving only a vacuous projection-identity proof rather than the actual rewrite. On top of that, the rule's core structure (pruning nested StructType fields, merging the metadata schema, ProjectionOverSchema mapping) has no representation in RuleScript's flat uninterpreted-type core language, which has no struct types or nested field access. ```
