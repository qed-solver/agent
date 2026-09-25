# AggregateFilterToCase

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToCaseRule.java
```

## Independent verifier review

**Verdict:** AGREE

The before-side pattern is an aggregate call carrying a SQL FILTER (WHERE ...) clause, which has no representation in the DSL's AggCall or in QED's JSON aggregate-call schema (operator/operand/distinct/ignoreNulls/type only) — a gap in the Rust prover's model, not something a Java-side builder can fix. Independently, the rule's soundness rests on aggregate NULL-ignoring algebra (agg over {a | cond} ≡ agg over {CASE cond THEN a ELSE NULL}), i.e. an aggregate algebraic identity QED explicitly cannot know: its uninterpreted aggregates only equate plans with identical groupings and bag-equal operand inputs, and these two operand bags differ by NULLs for every non-tautological cond, so no non-trivial special case becomes provable. The Filter-below-Aggregate alternative is correctly rejected since it drops groups with no matching rows, which FILTER semantics preserves. ```
