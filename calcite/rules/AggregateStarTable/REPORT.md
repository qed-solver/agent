# AggregateStarTable

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateStarTableRule.java
```

## Independent verifier review

**Verdict:** AGREE

This rule is a materialization substitution, not a relational identity: it replaces Aggregate(Scan(starTable)) with a Scan of a *separate* pre-aggregated table (optionally topped by a roll-up Aggregate or a reordering Project), and its soundness rests entirely on the planner lattice guaranteeing that the pre-aggregated table's rows equal the aggregation of the star table — a cross-table content invariant QED cannot express, since each scan is an independent uninterpreted relation whose schema carries only types, keys, and per-row guaranteed predicates (no builder or JSON field can relate one table's contents to another's, and the immutable prover has no such mechanism). The roll-up branch independently requires concrete SUM/COUNT roll-up aggregate algebra ("sum of sums = sum") that QED explicitly does not model for uninterpreted aggregate functions. The porter's diagnosis is correct and confirmed empirically: the two tables must be distinct symbols in any faithful encoding, the hint's fixable causes (name mismatch, missing composition, key/flag) genuinely don't apply, and the complete, non-timed-out `provable: false` run shows the gap is fundamental — and no narrower non-vacuous special case exists because the aggregate→scan-of-different-table substitution is the rule itself. ```
