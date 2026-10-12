# DecimalAggregates

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3191-3290
```

## Independent verifier review

**Verdict:** AGREE

Every arm is sound only by numeric facts about decimal types — a scale-preserving widening cast is value-identical/monotone (so it commutes with Min/Max), decimal Sum is homomorphic over the unscaled long sum (the Sum/MakeDecimal arms), and Average's double-division path is value-identical (the Average arms) — but QED encodes all casts/projections and aggregate functions as uninterpreted symbols with no arithmetic, monotonicity, or aggregate-algebra axioms (only input-bag equality), so even the smallest instance, e.g. `min(f(x)) ≡ f(min(x))` for uninterpreted f, admits a countermodel (non-monotone f) and is unprovable. The window arms additionally require Window, which has no bag semantics in QED at all. Since the prover's operator interpretation is fixed and unmodifiable and a DSL extension could only add more uninterpreted symbols, no encoding can close this gap — consistent with the verified ReduceDecimals and EliminateCast rejections of the same cast/numeric-identity class.
