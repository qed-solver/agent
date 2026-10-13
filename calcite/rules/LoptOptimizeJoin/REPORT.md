# LoptOptimizeJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 1  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/LoptOptimizeJoinRule.java
```

## Independent verifier review

**Verdict:** MANUAL

Manually investigated by the harness operator (the automated porter never got past turn 9 before crashing on context overflow — LoptOptimizeJoinRule.java is 2110 lines, too large for this model's 32K context to finish exploring regardless of restarts). Read the real Calcite source directly: onMatch() bundles four distinct sub-behaviors, not one rewrite: (1) findRemovableOuterJoins — drop a LEFT-join factor whose join key is unique and whose columns aren't projected downstream; (2) LoptSemiJoinOptimizer.chooseBestSemiJoin — a cost-based *selection* among semijoin filters (the underlying semijoin-filtering transformation itself is sound and already covered by this repo's existing *ToSemiJoin family, but WHICH one to apply is a cost heuristic, not an equivalence fact); (3) findRemovableSelfJoins — collapse two scans of the identical table joined on a unique key into one, which needs same-relation-identity reasoning beyond bag-equational proof; (4) the actual join-tree reordering search, which is a cost-based choice among already-equivalent trees — the underlying reassociation fact is exactly what DphypJoinReorder and MultiJoinOptimizeBushy already prove.

Of these, only (1) looked tractable as a standalone equivalence fact (not cost-dependent), so it was actually attempted: left.scan (nullable), right.scan declared UNIQUE on its join column, before() = Project(left-col-only, LeftJoin(left, right, EQUALS(left.col, right.col))), after() = left. This compiled and generated a structurally correct JSON (schema correctly shows right's key=[[0]]), but QED returned provable=false with smt_duration=0 — rejected before real SMT solving, i.e. this shape (2 base relations collapsing to 1 via a bare uniqueness argument) isn't in QED's equivalence-class procedure's recognized patterns. For comparison, the ALREADY-PROVED AggregateJoinRemove achieves a closely related 'drop a LEFT join' result differently: it wraps the join in a DISTINCT aggregate, so deduplication (not a direct uniqueness/cardinality argument) is what absorbs any row multiplication — that's a genuinely different, QED-provable proof technique, and it already exists in this repo. A bare (non-aggregated) join-elimination-via-declared-uniqueness, as LoptOptimizeJoin's sub-case (1) would need, is not something QED's decision procedure supports.

Conclusion: every piece of LoptOptimizeJoinRule.java's actual mathematical content is either already covered by existing proved rules (AggregateJoinRemove for join-removal-via-dedup, DphypJoinReorder/MultiJoinOptimizeBushy for join-tree reassociation) or is fundamentally a cost-based heuristic choice (semijoin selection, join-order search) that QED — which checks relational equivalence, not cost-optimality — cannot express a preference between. There is no additional provable content this rule would uniquely contribute.
