# CollapseGroupedSumOfCount

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/CollapseGroupedSumOfCount.scala, lines 35-124
```

## Independent verifier review

**Verdict:** AGREE

QED treats aggregate functions (COUNT, SUM, MAX, MIN) as uninterpreted symbols and can only equate aggregate calls via per-group input bag equality, so it cannot derive the identity this rule's soundness rests on — that summing per-fine-group COUNT(1) outputs over a coarser grouping equals a direct SUM(1) over the underlying rows (count additivity over a partition), nor the MAX/MIN idempotence over deduplicated grouping keys. This gap lives in the prover's fixed aggregate semantics, so no `extend_dsl_file` change to the Java DSL (which already serializes aggregate operator names and operands) can close it. I independently checked the narrowest possible special case — identical fine/coarse groupings with a single `SUM` — and it is still unprovable, since the outer SUM's per-group input on the before side is a bag of uninterpreted count values while on the after side it is a bag of literal 1s, which no SMT reasoning over uninterpreted functions can equate.
