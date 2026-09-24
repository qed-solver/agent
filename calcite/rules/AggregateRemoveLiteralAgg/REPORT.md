# AggregateRemoveLiteralAgg

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveLiteralAggRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire correctness argument is the Calcite-internal algebraic identity that LITERAL_AGG(lit) evaluates to lit on every group produced by GROUP BY (every produced group is non-empty), and QED models aggregates as uninterpreted functions whose only known behavior is bag-equality of inputs — it cannot derive that LITERAL_AGG's output equals the literal the after-side re-projects, so an SMT model with LITERAL_AGG ≠ lit is a valid counterexample to any faithful encoding. No narrowing fixes this (the dependency on LITERAL_AGG's bespoke semantics is present even in the minimal one-group-key, one-LITERAL_AGG(true) case), and the gap is not a missing DSL builder — the before/after shapes (scan, Aggregate with an AggCall over a literal operand, ProjectMany restoring fields plus a literal) are fully expressible with the current API — but a genuine limitation of QED's aggregate semantics, with no axiom/interpretation hook (table "guaranteed" constraints only constrain scan columns, and the prover itself is off-limits) that a DSL extension could supply. ```
