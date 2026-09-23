# MaterializedViewProjectJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectJoinRule.java
```

## Independent verifier review

**Verdict:** AGREE

MaterializedViewProjectJoinRule is a view-matching substitution, not an algebraic identity: it replaces a Project(Join(...)) query with a scan of a registered materialized view (or, with generateUnionRewriting, a Union of that view with incremental update deltas), so its soundness rests on the view being *defined* as a query over the same base tables plus MV freshness/delta semantics. RuleScript tables are independent uninterpreted symbols — nothing in the language (or even the JSON schema's "guaranteed" constraints, which are row-level predicates over a table's own columns) can express that one table's contents equal an expression over other tables — and QED's bag-semantics model has no notion of temporal deltas, so for arbitrary instantiations the MV scan is just an unconstrained relation and no before/after encoding could ever be proven equivalent.
