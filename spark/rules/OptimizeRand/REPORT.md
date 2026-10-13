# OptimizeRand

**Status:** FAILED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 102  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeRand.scala, lines 33-63
```

## Independent verifier review

**Verdict:** REJECTED

The provable claim is only that `Filter(T, T.col < 1)` is redundant for a one-column table `T` whose check constraint already guarantees `col < 1`; that is a constraint-redundancy rule, not Spark's expression-level rewrite of `Rand()` comparisons. It does not model an arbitrary input plan with a `Rand` expression having range [0,1), and it hard-codes the random value as the scanned column, so the proof is of a different pattern. A faithful encoding would either introduce an arbitrary source plus an uninterpreted `Rand` predicate/function with its range as an explicit axiom, or declare the full rule unsupported if QED cannot model that function semantics.
