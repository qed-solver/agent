# MaterializedViewOnlyJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyJoinRule.java
```

## Independent verifier review

**Verdict:** AGREE

MaterializedViewOnlyJoinRule is not a closed-form pattern-to-pattern logical rewrite — its validity rests on a side condition that an externally defined materialized-view table (an independent uninterpreted scan symbol in the DSL) is maintained as the bag result of a specific query over the base tables, a table-equals-derived-relation fact that RuleScript's core language has no construct to state (no view/definition operator; "guaranteed" table constraints are only row-wise scalar predicates, not relation equality), and on compensation predicates derived via predicate entailment between uninterpreted predicate symbols, which QED explicitly cannot reason about. The only possible encoding (before: Join over base tables; after: scan of the MV table) is not universally bag-equivalent — the prover would find a trivial countermodel (e.g. empty MV, nonempty join) — so no re-encoding can be proved, and the porter's conclusion (though actually caused by an LLM context-length error rather than a QED verdict) is correct.
