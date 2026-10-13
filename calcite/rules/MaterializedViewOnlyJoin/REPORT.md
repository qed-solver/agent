# MaterializedViewOnlyJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyJoinRule.java
```

## Independent verifier review

**Verdict:** AGREE

MaterializedViewOnlyJoin is a catalog-driven materialized-view rewrite, not a closed-form algebraic identity: its validity rests on the external fact that a stored MV table's contents are *defined* to equal a particular derived query (the matched join/input) together with predicate/column entailment between that MV's defining query and the join subtree. In RuleScript a `scan` is an atomic uninterpreted symbol with no definitional link to any derived relation, so QED treats the MV scan and the join input as independent symbols and — per its stated limitation of not reasoning about predicate entailment between independent symbols — cannot prove their equivalence; and because the "after" side depends on *which* MV matched and on a per-match column/predicate mapping, there is no single fixed before()/after() pattern pair whose bag-equivalence QED could universally establish, so even a narrower special case is not expressible.
