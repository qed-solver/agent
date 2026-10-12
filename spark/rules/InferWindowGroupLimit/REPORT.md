# InferWindowGroupLimit

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InferWindowGroupLimit.scala, lines 54-138
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on order-dependent semantics that QED by design does not model: `WindowGroupLimit` keeps the first `limit` rows of each partition under `orderSpec`, and row_number/rank/dense_rank assign ordinal positions within that ordering — the equivalence holds only because `rank <= limit` selects exactly that order-prefix (with tie behavior handled by the retained filter). QED is a bag-semiring decision procedure that explicitly lacks ordering semantics for Limit/Offset/Order By (qed.pdf §6.2), has no Window/rank-frame operator in the DSL or the prover's theory, and cannot perform predicate inference between independent uninterpreted symbols, so even a faithful encoding with `WindowGroupLimit` and the rank column as uninterpreted symbols leaves the critical prefix↔rank relation as an unknowable internal semantic. This is a fundamental limitation of the Rust prover's theory, not a missing builder that `extend_dsl_file` could close. ```
