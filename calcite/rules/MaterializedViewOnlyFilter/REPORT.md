# MaterializedViewOnlyFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewOnlyFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness is exactly the catalog invariant that a materialized view's contents are maintained to equal its defining plan over the base tables — a cross-table "defined-as" hypothesis coupling two otherwise independent free table symbols — and QED decides bag-equivalence universally over *all* instantiations of free tables (schemas carry only per-table keys/constraints, no relational hypotheses between tables), so any after-side that keeps the MV as a distinct scan is not provably equivalent to the query over base tables. This is a limitation of the prover's theory, not of the DSL's surface: no `extend_dsl_file` change to RelRN/JSONSerializer can add an SMT assumption the fixed Rust prover does not read, and the JSON format has no cross-table defined-as field for it to consume. Inlining the MV's defining plan into both patterns would make something provable, but the "rule" degenerates into an ordinary logical rewrite over that plan (e.g. filter composition) with the precomputed artifact erased — i.e. the MV rule's actual content is lost, so UNSUPPORTED is the correct conclusion.
