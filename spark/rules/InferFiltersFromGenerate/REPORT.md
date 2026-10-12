# InferFiltersFromGenerate

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 33  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2191-2227
```

## Independent verifier review

**Verdict:** AGREE

The rule is sound only because Spark's Explode/Inline generators emit zero rows when the generator input is NULL or an empty array, so rows failing `size(x)>0 ∧ x IS NOT NULL` contribute nothing to the bag below the generate — that is a backend operator's bespoke internal semantics. QED's format has no row-multiplying/table-function node (only scan/filter/project/join/correlate/group/union/sort), and its only stand-in, an uninterpreted projection, lacks the drop behavior, so the prover's refutation of the porter's encoding is a genuine counterexample (a P-false row still emits `gen(r)` on the left), not a symbol-sharing bug. Since QED also cannot do predicate entailment between independent uninterpreted symbols, even a join-based model of element enumeration could not be axiomatically linked to the inferred filter, so the rule is fundamentally unexpressible to the prover.
