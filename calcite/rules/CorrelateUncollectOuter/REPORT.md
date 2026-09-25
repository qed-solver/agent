# CorrelateUncollectOuter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CorrelateUncollectOuterRule.java
```

## Independent verifier review

**Verdict:** AGREE (manual)

Manually investigated by Claude (not the automated porter/verifier LLM loop, which exhausted all 5 rounds on repeated context-length crashes without ever reaching a try_rule call). Read the source (CorrelateUncollectOuterRule.java): it moves the outer-join (null-extension-on-empty-array) semantics of a Correlate(joinType=LEFT) over an Uncollect down into the Uncollect operator's own isOuter flag, then relaxes the Correlate to joinType=INNER (since Uncollect(isOuter=true) now unconditionally guarantees at least one output row per input row, so INNER vs LEFT no longer changes cardinality). This requires representing Uncollect (array/multiset unnesting, e.g. UNNEST) as a relational operator with a configurable outer/empty-array behavior. Checked RuleScript's entire DSL (RelRN.java, RexRN.java, JSONSerializer.java/JSONDeserializer.java) and QED's prover core (qed-prover/src/pipeline/relation.rs's Relation enum: Singleton, Scan, Filter, Project, Join, Union, Intersect, Except, Distinct, Values, Sort, Aggregate, Correlate) — there is no array/multiset/Uncollect concept anywhere in either the DSL or the prover's data model. This is a fundamental representational gap, not a missing clever encoding: the operator this rule rewrites simply has no counterpart to construct in RuleScript at all, so there is no candidate encoding to even attempt.
