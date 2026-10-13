# MaterializedViewProjectFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

An MV-substitution rewrite is sound only under the external, planner-maintained invariant that the MV table's contents equal its defining query over the base tables (plus freshness/lineage state), but in RuleScript every scan is an independent uninterpreted bag and QED decides unconditional equivalence under arbitrary instantiations of all table symbols — its JSON interface carries only per-table `guaranteed` constraints (e.g. the `unique` flag's key), with no premise/assumption channel for a cross-table equality between two scans. The rule file itself contains no fixed transformation (it merely matches `Project(Filter(...))` and delegates to `perform()`, which consults the planner's MV registry at match time), so any encoding either keeps the MV as a separate symbol (making the substitution non-equivalent under universal instantiation, hence unprovable) or reuses the base-table symbol (erasing the "materialized" relation and no longer expressing this rule). Since the gap lies in the unmodifiable, premise-free prover rather than in the Java DSL, `extend_dsl_file` cannot close it, and the porter's UNSUPPORTED conclusion — despite the transcript showing it never actually ran a candidate through QED — is the correct one. ```
