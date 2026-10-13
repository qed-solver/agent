# OptimizeRand

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeRand.scala, lines 33-63
```

## Reason

The rewrite `rand() < 1.0 -> true` / `rand() > 1.0 -> false` etc. is sound only because Rand() is known to produce values in [0, 1). QED treats a nondeterministic scalar function as an opaque uninterpreted function with no range axiom, and the only place the prover accepts value-range facts is a table-level check constraint on a scanned column, which describes a stored column rather than an expression. Encoding Rand as a constrained column proves a different pattern (redundant filter over a constrained table), and without the range fact the rewrite is not an equivalence for an uninterpreted function. No faithful narrowing exists.
