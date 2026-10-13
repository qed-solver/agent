# OptimizeRepartition

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2045-2053
```

## Independent verifier review

**Verdict:** AGREE

QED decides equivalence of row bags over Q-expressions, and that model has no notion of physical partitioning/distribution or of per-operator physical attributes such as partition count — the DSL/JSON surface (scan/filter/project/join/correlate/aggregate/union/intersect/minus/sort) has no repartition/exchange node, and even a `extend_dsl_file` builder couldn't help since the immutable Rust prover would have to interpret such an attribute and simply doesn't model it. OptimizeRepartition's sole effect is flipping `optNumPartitions` from None to Some(1) (sound only because hash-partitioning on foldable/constant expressions sends every row to one bucket — a physical fact QED cannot see), while the output row bag is identical to the child's before and after. Any RuleScript encoding would therefore degenerate to an identity rewrite (child ≡ child) that captures neither the rule's effect nor its foldable-expressions guard, so the UNSUPPORTED call is correct.
