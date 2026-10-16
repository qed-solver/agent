# MaterializedViewOnlyFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

This is not an algebraic equivalence between two plans but a catalog-driven rewrite whose soundness rests on the external planner invariant that the registered MV table is a current materialization of its defining query (bag(MV) = bag(defineQuery(base tables))), and that invariant cannot be stated as a premise in QED's theory. Table scans are independent uninterpreted bags whose only declared properties are per-table primary keys and single-table check predicates (`key`/`guaranteed` in the JSON), and neither the RuleScript DSL nor the prover's JSON schema has any construct for a table-to-plan or cross-table identity — closing that gap would require changing the trusted Rust prover, which is off-limits, not a Java-side `extend_dsl_file` fix. So any encoding that keeps the MV as a distinct scan symbol is unprovable, while identifying the MV scan with the defining-subplan symbol degenerates the rule to a vacuous filter rearrangement; the porter's UNSUPPORTED conclusion and its stated reason are correct.
