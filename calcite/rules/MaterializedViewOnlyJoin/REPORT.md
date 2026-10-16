# MaterializedViewOnlyJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 29  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyJoinRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite substitutes a query join subplan with a scan of a separately registered MV table (cross-joined with query-only tables), and its entire correctness rests on the external planner invariant that the MV table's contents equal its defining query — a cross-table identity between two independent uninterpreted relations. QED's table schema format only carries per-table keys and single-table "guaranteed" constraints (serialized in a Rex environment over that table's own row type), so the invariant cannot even be stated as a premise, and the SMT decision procedure over independent uninterpreted relations cannot derive it; identifying the MV scan with the subplan symbol degenerates the rule to the vacuous identity, so no non-trivial provable encoding exists. ```
