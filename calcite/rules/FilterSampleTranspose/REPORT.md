# FilterSampleTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSampleTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The validity of Filter(c, Sample(R)) ⟹ Sample(Filter(c, R)) rests on sampling being a per-row, probabilistic selection whose keep/drop decision is independent of the row's attributes — QED is a deterministic bag-semantics decision procedure whose own reference lists Sample (with Sort/Limit/Offset/Window) as having no bag-semantic meaning, and whose unchangeable Rust prover has no sample/probability concept. The JSON format has no Sample node and no DSL parameter (rate/seed) to carry, so no `extend_dsl_file` can close the gap — the only encodable surrogate, Sample-as-uninterpreted-row-predicate, would merely prove trivial filter commutativity (Filter(c, Filter(s, R)) ⟹ Filter(s, Filter(c, R))) for two arbitrary filters, which says nothing about what makes an operator a sampler, so the porter's UNSUPPORTED conclusion is correct. ```
