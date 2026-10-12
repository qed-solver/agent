# CollapseRepartition

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** only Case 1's "keep the top node" branch is covered (a Repartition whose child is a Repartition/RepartitionByExpression collapses to the outer repartition over the grandchild); Cases 2-3 require eliminating a Sort child, which QED cannot model (no list/ordering semantics), and Case 1's coalesce branch's numPartitions guard is a physical condition, not a bag-level fact.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2013-2039
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful: each Repartition is modeled as an identity projection, which is exactly the bag-semantic content of a shuffle node, so `before()` (stacked identity projections) and `after()` (a single one) are structurally distinct and the proof genuinely captures the "drop the redundant repartition" optimization rather than being a vacuous before==after. The covered branch (`case _` of Case 1, keep-top) has no bag-level precondition that is missing — the numPartitions guard exists only in the excluded (coalesce, shuffle) branch — and reusing one identity symbol for both stacked nodes is safe because the proved statement abstracts the partition-count parameter that the rewrite does not depend on for bag equality. The scope is honestly marked PARTIAL: the restriction is genuine and specific (Cases 2–3 require eliminating a Sort, which QED cannot model; the excluded branch is a physical comparison), and the covered rule is a non-degenerate, useful special case; the only mild narrowing (fixed 3-column arity) is structural, not semantic, and is unavoidable in the DSL.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 85292
  }
}
```
