# ExtractPythonUDTFs

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 80  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 401-420
```

## Independent verifier review

**Verdict:** AGREE

QED's Q-expression/semiring calculus has no table-generating (row-producing) operator — nothing that, per input row, yields a data-dependent set of fresh output rows correlated to that row (a SQL LATERAL/UDTF/UNNEST) — yet both the before-side `Generate(PythonUDTF, …)` and the after-side `BatchEvalPythonUDTF`/`ArrowEvalPythonUDTF` are exactly such UDTF nodes, so neither pattern is even expressible and the rewrite's correctness rests entirely on the UDTF's row-generation semantics, a bespoke internal behavior QED cannot see through as an uninterpreted function. This gap is not closeable via `extend_dsl_file`: `JSONSerializer` has no serialization case for a table-function operator and the unmodifiable Rust QED prover has no Q-expression node for one, and `correlate` does not substitute because it is a correlated join over a fixed independent right relation, not a per-row function application producing a variable number of rows. ```
