# SubQueryRemove

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 1  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SubQueryRemoveRule.java
```

## Independent verifier review

**Verdict:** MANUAL

Manually investigated by the harness operator, going further than the earlier automated verdict. SubQueryRemoveRule.java is 1488 lines dispatching on 6 sub-query kinds (SCALAR_QUERY, ARRAY/MAP/MULTISET_QUERY_CONSTRUCTOR, SOME, IN, EXISTS, UNIQUE); most genuinely need real aggregate-function algebra (SINGLE_VALUE, MIN/MAX, COUNT, three-valued CASE/null logic) that QED does not model. But the narrowest slice — rewriteExists' Logic.TRUE branch, i.e. Filter(EXISTS(sub), outer) as the sole filter condition, uncorrelated — only uses GROUP BY a constant marker column with ZERO aggregate calls to collapse the sub-query to at-most-one-row, exactly the same dedup-absorbs-duplication technique the already-proved AggregateJoinRemove relies on. So this specific slice looked genuinely tractable, and the earlier verdict's stated blocker (RexRN has no subquery node) is a real but CLOSABLE DSL gap, not a fundamental one — JSONSerializer.java already has a full RexSubQuery serialization branch.

This was actually attempted: added RexRN.Exists(RelRN) — a small extend_dsl_file-style addition to RexRN.java constructing a genuine Calcite RexSubQuery.exists(...) — then encoded before() = Filter(EXISTS(sub), outer), after() = InnerJoin(outer, Aggregate(Project(TRUE, sub), groupKey=[0], no agg calls)) on a TRUE condition, mirroring rewriteExists' TRUE-logic branch line for line. This compiled clean and produced a structurally correct JSON (confirmed by inspecting it directly: the EXISTS operator carries a genuine nested 'query' sub-relation, exactly as JSONSerializer.java's RexSubQuery branch is designed to emit). But QED's own prover rejected it with provable=false and smt_duration=0 — rejected before real SMT solving even started, the same fast-rejection signature seen when an encoding falls outside the equivalence-class matcher's recognized shapes (as with the separately-investigated LoptOptimizeJoin). Checked the actual Rust prover source (qed-prover/src/pipeline/relation.rs:551): Expr::Op{rel: Some(_), ..} — i.e. any expression containing a nested sub-relation — is explicitly marked complete() = false, and such expressions are evidently not handled by the equivalence-class stage at all, independent of whether the underlying mathematical content (dedup-based cardinality collapse) is itself something QED can reason about in other contexts.

Conclusion: the DSL-level gap (no subquery builder) is real but was successfully closed here. The deeper, genuinely fundamental blocker is that QED's decision procedure cannot reason about RexSubQuery-containing expressions at all, confirmed directly against the prover's own source, not inferred from a failed proof attempt alone. This applies to every sub-query kind this rule handles, not just EXISTS, so no sub-case of SubQueryRemove is provable with the current prover backend.
