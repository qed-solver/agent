# TableScan

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/TableScanRule.java
```

## Independent verifier review

**Verdict:** AGREE

TableScanRule's only non-trivial case (view expansion via `table.toRel`) asserts that a scanned table's contents equal the relational plan stored in its table metadata, and QED models every scan as an independent uninterpreted bag — RuleScript/JSON has no construct tying a table symbol to a defining plan (the "guaranteed" field carries only row-level predicates, not bag equality), so the required equivalence is fundamentally inexpressible/invisible to the prover, not a fixable symbol-sharing or DSL gap. The porter's concrete non-trivial encoding returned a definitive not-provable (complete fragment, no SMT timeout), and the only provable instance is the vacuous identity rewrite scan→scan, which captures none of the rule's actual transformation. ```
