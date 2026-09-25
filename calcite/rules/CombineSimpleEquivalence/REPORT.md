# CombineSimpleEquivalence

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CombineSimpleEquivalenceRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on the Spool's producer→consumer binding — a sibling branch's `LogicalTableScan` must denote exactly the bag the `TableSpool` materialized — and QED's model cannot express that: its input format defines only independent base tables (with at most per-row scalar "guaranteed" constraints) over a fixed operator set (scan/values/filter/project/join/correlate/group/union/intersect/except/sort), with no let/define/spool operator that could tie an auxiliary table to a sub-expression's output. Since the QED prover itself is the unmodifiable arbiter and has no such semantics, no `RelRN`/`JSONSerializer` extension can close the gap — the consumer scan would remain an arbitrary table unrelated to the producer's output, so before/after cannot be shown bag-equivalent even in the narrowest special case (shared component = a plain scan, spool table still independent of the source table).
