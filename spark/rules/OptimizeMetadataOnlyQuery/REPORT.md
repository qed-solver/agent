# OptimizeMetadataOnlyQuery

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/OptimizeMetadataOnlyQuery.scala, lines 46-182
```

## Independent verifier review

**Verdict:** AGREE

The rewrite swaps a data-table scan for a *different* partition-metadata-table scan, and its soundness rests on the storage/catalog invariant that the metadata table holds exactly one row per distinct partition of the data table — but QED models two differently-named scans as independent uninterpreted relations that are universally quantified over, and its per-table `guaranteed` constraints are Rex expressions over a single table's columns only, so no cross-table "metadata = distinct(data)" relation can be asserted, meaning the equivalence must (falsely) hold for arbitrary independent instantiations. Even if the after-side were recast as a DISTINCT over the *same* scan, the rule would still require Max/Min/Count-Distinct to be multiplicity-insensitive (its result over the full bag equals its result over the bag's distinct elements) — an aggregate-algebra identity the prover explicitly does not model, since it only equates aggregate results when the input bags are provably equal, and here the bags differ. Both gaps are in the unmodifiable prover's semantics (independent-scan quantification + uninterpreted aggregates), not a missing DSL operator, so extending the DSL (a Distinct operator or a cross-table constraint) could not close them and a faithful encoding is genuinely unprovable.
