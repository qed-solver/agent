# MaterializedViewProjectFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/materialize/MaterializedViewProjectFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on an external data invariant — the materialized view's rows equal its defining query over the base tables, plus the query-to-view table/lineage mapping — which is planner-catalog state, not plan semantics: RuleScript rules are self-contained before/after pairs and QED decides equivalence for every instantiation of their symbols with no premise/assumption channel (table annotations are only keys and single-table "guaranteed" predicates), so any encoding that reads the MV as a separate uninterpreted table is unprovable (QED would treat the MV as an arbitrary relation unrelated to the query), and the only provable degenerate form — inlining the MV's definition, which collapses the rule to the filter-splitting identity π(σ_P(A)) ≡ π(σ_{Q∧P}(A)) ∪ π(σ_{¬Q∧P}(A)) — contains no materialized view at all and hence does not express this rule. (The porter's UNSUPPORTED line was actually emitted by an LLM context-length HTTP error rather than a technical conclusion, but the conclusion itself holds on the merits.) ```
