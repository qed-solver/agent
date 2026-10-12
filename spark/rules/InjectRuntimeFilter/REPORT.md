# InjectRuntimeFilter

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InjectRuntimeFilter.scala, lines 38-466
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness rests on the bloom filter's no-false-negative invariant — `BloomFilterMightContain(agg(right_keys), hash(l.key))` must hold for every left row that satisfies the join condition — and QED models both `BloomFilterAggregate` (a specific aggregate whose algebra it knows nothing about) and `BloomFilterMightContain` as uninterpreted, so under an interpretation where the predicate is identically false the after-plan is a strict sub-bag and equivalence is genuinely not derivable. This is the "backend operator's bespoke internal semantics / aggregate algebra" limitation, compounded by the fact that table `guaranteed` constraints are scoped to a table's own columns and cannot express the needed cross-table link between the join condition and the filter; adding a scalar-subquery builder via `extend_dsl_file` would not add the missing axioms, and the prover itself is off-limits, so no non-vacuous encoding (the only provable one is the no-op filter = true) can capture the rule.
