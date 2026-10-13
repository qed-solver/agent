# MaterializedViewProjectAggregate

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectAggregateRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness depends on the catalog's MV-maintenance invariant — the MV's stored bag equals its defining aggregate over the base tables — and RuleScript offers no way to constrain two distinct uninterpreted tables (same-name = same symbol is the only coupling, and reusing one scan would degenerate the rule into an identity), so `Scan("MV") ≡ Aggregate(Project(Scan("S")))` is genuinely false under QED's universal quantification rather than unprovable. Compounding this, the rule's rollup compensation (re-aggregating the MV, e.g. SUM-of-SUM over a coarser grouping) requires aggregate-algebraic identities that QED explicitly cannot model for uninterpreted aggregate symbols, so even a hypothetical table-invariant extension would not make the compensation provable; no narrower expressible special case preserves the rule's actual meaning. ```
