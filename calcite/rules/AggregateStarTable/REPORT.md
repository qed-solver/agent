# AggregateStarTable

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateStarTableRule.java
```

## Independent verifier review

**Verdict:** AGREE

Every branch of this rule (exact match, roll-up, or measure reordering) rewrites `Aggregate(base)` into a plan over a *different* scan whose equality to the base table's aggregation is an external lattice/catalog invariant, not an operator semantics — and QED's verdict is an *unconditional* bag-equivalence over independently-instantiated uninterpreted relations, whose only admitted side information is per-table keys and per-table first-order `guaranteed` check constraints, so the second-order premise "materialized table = aggregation of base table" cannot be expressed or accepted (extending `RelRN`/`JSONSerializer` can't help, since that premise would require semantics only the unmodifiable Rust prover could provide). Moreover, the roll-up branch additionally depends on aggregate roll-up algebra (e.g. sum-of-sums), which QED explicitly does not model, so no narrower sub-case of the rule is provable without the invariant — the porter's UNSUPPORTED verdict is correct.
