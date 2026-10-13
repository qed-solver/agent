# RewriteExceptAll

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3445-3471
```

## Independent verifier review

**Verdict:** AGREE

The rule's LHS is bag/multiset EXCEPT ALL, which QED fundamentally does not model — it supports only the set variant of MINUS (the serializer throws on `minus.all == true`, and the prover deliberately omits a semiring monus), so the before() side cannot even be serialized, and no core-language expression can replicate count differencing since QED's semiring has no subtraction. Independently, the RHS requires numeric aggregate algebra (SUM of +1/−1 tags, `> 0` comparison) and Generate(ReplicateRows) count-based row replication: QED treats aggregates as uninterpreted beyond input bag equality, the DSL exposes no arithmetic operators (and `Const` serializes all values opaquely), and variable-count row replication has no bag-semantic meaning QED models. Since every instance of this rule has the unmodelable bag minus on the LHS, no narrower special case is encodable either.
