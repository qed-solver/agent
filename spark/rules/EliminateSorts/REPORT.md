# EliminateSorts

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 47  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2430-2502
```

## Independent verifier review

**Verdict:** AGREE

Every branch of EliminateSorts — subsumption of lower sorts under a global sort, joins re-ordering/destroying input order, sorts below order-irrelevant aggregates (Min/Max/Count/non-float Sum) being useless, and foldable-key sorts being no-ops — is justified solely by row-order/list semantics, but QED is a bag-semantic decision procedure with no model of Sort/Limit/Window ordering and no algebra for aggregate functions beyond bag equality of their inputs. No faithful encoding exists: the only "proof" obtainable would be the degenerate Sort ≡ identity claim (which would be a *false* backend rewrite for a top-level ORDER BY), not the rule itself, and adding a Sort builder via extend_dsl_file cannot close the gap because the unmodifiable prover has no ordering logic in which to check such a rewrite — this is a fundamental QED limitation, exactly the class the reference marks as unsupported. ```
