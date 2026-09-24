# AggregateExpandWithinDistinct

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateExpandWithinDistinctRule.java
```

## Independent verifier review

**Verdict:** AGREE

QED treats every aggregate operator as uninterpreted and only proves aggregate equality via bag-equality of the inputs, but this rule's core step — equating an outer SUM/COUNT over per-(group, distinct-key)-subgroup MIN results with the before-side WITHIN DISTINCT aggregate computed directly over the input rows — requires knowing that MIN selects the functionally-dependent unique value and that SUM over subgroup minima equals SUM over the deduplicated values, which is exactly the aggregate algebra QED explicitly cannot know. The rewrite is also only sound under a user-asserted functional dependence (distinct key functionally determines the argument value within each group) — a side condition Calcite itself guards with THROW_UNLESS — and QED proves for all instantiations, so the FD cannot be assumed away; a countermodel with two rows sharing (group, distinct-key) but differing argument values refutes any universal proof. On top of that, the DSL/JSON carries no WITHIN DISTINCT or GROUPING SETS at all (the serializer drops `distinctKeys`), and since the QED prover is the immutable arbiter that would need a model for both, even the minimal single-grouping-set special case (outer `f(MIN(x))` over inner `Aggregate(group ∪ D, MIN(x))`) is unprovable — so the UNSUPPORTED conclusion is correct, not a product of the porter's context-length failure.
