# RewriteNonCorrelatedExists

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/finishAnalysis.scala, lines 69-79
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's correctness rests entirely on Limit(1, ·) converting "Q is non-empty" into a well-defined single-row scalar-subquery result, but QED assigns Limit no bag-semantic meaning — it becomes a fully uninterpreted QOp(Limit, 1, Q) with no axioms tying its output bag (size, non-emptiness) to its input, and the EXISTS / ScalarSubquery / IsNotNull stack on either side likewise cannot be interpreted as bag non-emptiness, leaving two independent uninterpreted atoms the SMT solver cannot relate. This is a fundamental QED limitation, not a missing DSL builder: extend_dsl_file only changes how patterns are serialized into JSON, while the trusted Rust prover is frozen and will never receive non-emptiness/cardinality axioms for Limit. No faithful narrowing exists either — dropping the load-bearing Limit changes the test's truth condition from ‖Q‖ > 0 to ‖Q‖ = 1 (error for more), so the porter's UNSUPPORTED conclusion is correct. ```
