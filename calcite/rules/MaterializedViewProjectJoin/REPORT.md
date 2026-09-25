# MaterializedViewProjectJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectJoinRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's only non-trivial content is substituting a scan of a catalog-defined MV for the Project(Join) it materializes, and its soundness rests on the external catalog invariant that the MV table's contents equal its defining query (modulo freshness/delta in the union-rewriting variant); in QED's semantics a table scan is an uninterpreted symbol instantiable as any arbitrary relation, and neither RuleScript nor the immutable prover's JSON format has any facility to assert a definitional equality between a scan and a derived relation (nor any temporal/delta model), so no before()/after() pair — not even a narrower special case — would hold for all symbol instantiations. The only provable residue here (e.g., pushing a projection that references one join side through the join) is a different rule (ProjectJoinTranspose), not MV substitution.
