# DecimalAggregates

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3191-3290
```

## Independent verifier review

**Verdict:** AGREE

The claim holds: every arm rests on decimal arithmetic QED cannot know — aggregates are equatable only via bag-equality of their inputs (and every arm changes that input bag, e.g. Min(Cast(x)) vs Cast(Min(x)), where the cast is an uninterpreted function with no monotonicity/injectivity, so the commutation isn't even valid in QED's theory), and Cast/MakeDecimal/UnscaledValue/Divide are uninterpreted symbols with no arithmetic or ordering, making the Sum/Average scaling-and-division identities unprovable by construction. The window arm additionally needs window/list semantics QED explicitly lacks, and this is a real QED limitation rather than a missing DSL builder: the prover is a fixed Rust project whose JSON vocabulary has no way to attach numeric axioms (monotone, scaling, power-of-10 division) to uninterpreted functions, so no Java-side extension could make any arm provable. ```
