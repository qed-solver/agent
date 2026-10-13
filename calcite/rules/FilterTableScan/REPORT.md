# FilterTableScan

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterTableScanRule.java
```

## Independent verifier review

**Verdict:** AGREE

FilterTableScanRule is a purely physical pushdown (Filter(TableScan) → BindableTableScan) whose before and after differ only in representation — the BindableTableScan's output rows are *defined* by the backend FilterableTable/BindableTableScan contract to equal the logical Filter-over-Scan, so there is no logical algebraic identity for QED to verify. The bag-semantic core language has no physical/native-filtered-scan operator, so the after-side can only be encoded as the textually identical Filter(P,Scan) (a tautology that proves nothing about the rewrite) or as an uninterpreted operator to which the fixed QED prover has no axiom linking it back to Filter(P,Scan) — and this gap is a genuine limitation of QED's logical model, not a missing DSL builder that `extend_dsl_file` could close, since the physical operator has no distinct logical identity at all. ```
