# MaterializedViewProjectFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's before pattern (Project over Filter over base tables) and after pattern (projection/filter over the MV scan) are individually expressible, but the rewrite's soundness rests solely on the cross-table invariant bag(MV) = Project(Filter(..., base)) — that the maintained MV table's bag equals its defining query over the base tables. QED assigns every scan an independent uninterpreted bag, and the full schema vocabulary the serializer can emit (per-table "key" sets and "guaranteed" check predicates, each scoped to its own table's columns via a Rex env with that table's field-count delta) has no mechanism to relate two distinct table symbols or assert bag equality between them, so no DSL-level extension can supply the missing premise without modifying the trusted prover itself. The only encoding that would prove something would collapse the MV and base into a single shared scan symbol, which eliminates the rule's premise entirely and degenerates into an ordinary filter/predicate identity (a different rule), confirming UNSUPPORTED. ```
