# RewriteSetComparison

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 66  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/rewrite_set_comparison.rs, lines 1-175
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire semantic content is a scalar three-valued-logic identity — `x op ANY/ALL (S)` being equal to a CASE over EXISTS subqueries with IS TRUE / IS NULL / NULL-literal branches — and that is precisely the level QED abstracts away: predicates and comparisons are uninterpreted symbols with two-valued boolean output in the SMT encoding, there is no CASE/NULL/3VL operator or axiom mechanism in the prover to define ANY/ALL in terms of EXISTS, and even a maximal `extend_dsl_file` addition of those builders would only yield independent uninterpreted symbols whose equivalence the prover cannot decide (the stated "predicate entailment between independent symbols" / "bespoke operator internals" limitations). I confirmed against the DSL source that no EXISTS/scalar-subquery predicate, CASE, NULL literal, IS-TRUE/IS-NULL, or set-comparison node exists (the `RexSubQuery` hits are serializer/deserializer plumbing, and `Correlate` is a relational dependent join producing pair-rows, not a boolean EXISTS test), and the only fully-relational re-encoding of both sides collapses to semi-join ≡ semi-join, a tautology that discards the rule's content — so no genuine, non-trivial provable special case exists, and the gap is a fundamental prover limitation, not a missing builder.
