# CombineApproximatePercentiles

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CombineApproximatePercentiles.scala, lines 73-225
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's only non-trivial step replaces two scalar `approx_percentile` calls with one fused array-valued call plus `GetArrayItem` extractions, and its correctness is the engine-internal identity "element i of the fused digest equals the scalar percentile p_i over the same input bag" — an algebraic axiom between two distinct aggregate functions, while QED treats every aggregate as an opaque uninterpreted symbol and relates different symbols only through bag equality of their inputs. Compounding this, QED has no list/array semantics at all, so the fused digest and its element access cannot even be expressed in the core language, and `extend_dsl_file` cannot close that gap because the missing semantics would have to live in the immutable prover. The only encoding QED could prove would reuse a single aggregate symbol with identical arguments on both sides (a tautological identity that doesn't correspond to the actual rule, whose whole point is fusing calls with different percentage arguments), so no genuine special case is provable.
