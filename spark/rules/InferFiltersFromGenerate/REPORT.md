# InferFiltersFromGenerate

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2191-2227
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness rests on the generator's internal list semantics — Explode/Inline emits exactly zero rows iff its input is NULL or an empty list — and QED's bag-semantic theory over uninterpreted functions has no list type, no row-generation/unnest operator, and no axiom connecting a (necessarily uninterpreted) Generate operator to the `size(x) > 0 AND x IS NOT NULL` predicate, so any encoding in which Generate is an uninterpreted operator admits an SMT countermodel. The relational workaround of decomposing Explode into a join with an element-membership table also fails: the filter the rule inserts would need a "has at least one element" existential/size condition that the core language cannot express (no exist/subquery connectives, and QED cannot derive count=0 ⟺ empty input for aggregates), so this is qed.pdf's explicit "List semantics" limitation of the unmodifiable prover, not a missing builder that extend_dsl_file could close. ```
