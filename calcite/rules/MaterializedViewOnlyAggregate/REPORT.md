# MaterializedViewOnlyAggregate

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 28  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyAggregateRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's sole correctness premise is the registration-time invariant that a separately registered, externally refreshed MV table's contents equal its defining aggregate — a cross-table fact about a planner-registered artifact, not a relational-algebra identity QED can derive; QED treats the MV scan as an independent uninterpreted table, and the "guaranteed" constraints in the JSON format only carry single-table predicates, so no non-vacuous before/after pair (MV modeled as a distinct scan fails; modeling it as the same symbol degenerates the rule to the identity rewrite) can be proved. Its rollup/compensation paths independently require re-aggregation algebra (count→sum0, SUM/COUNT distribution through a finer-grained grouping) that QED explicitly does not model beyond bag equality of aggregate inputs, and the intermediate group-by changes that input bag — a fundamental limitation, not a DSL gap an extend_dsl_file attempt could meaningfully close. ```
