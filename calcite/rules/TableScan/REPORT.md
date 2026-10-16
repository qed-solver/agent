# TableScan

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/TableScanRule.java
```

## Independent verifier review

**Verdict:** AGREE

TableScanRule's entire correctness content is the identity "the bag of table V equals the relation produced by V's RelOptTable.toRel" (its view definition over *other* table symbols), and QED's semantics make every named table an independent uninterpreted bag whose only admissible side conditions are per-table key and check predicates (the JSON schema carries only "key"/"guaranteed" per table, with guaranteed predicates scoped to that table's own row) — there is no operator, constraint kind, or JSON construct in the DSL to assert a table-to-expression identity, so any encoding is either the vacuous base-table tautology Scan(T) = Scan(T) (proving nothing) or unprovable independent-symbol inequality. This is a fundamental limitation of the trusted prover's table model, not a missing Java-builder gap closable via extend_dsl_file, and even the degenerate alias-view special case (V = scan of B) still requires the same inexpressible cross-symbol bag equality.
