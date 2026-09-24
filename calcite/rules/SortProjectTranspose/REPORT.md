# SortProjectTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortProjectTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The entire semantic content of SortProjectTranspose is row-ordering: the sort must be remapped through the projection's collation (with offset/limit carried along), which is only valid under structural side conditions — every sort key maps to a plain input reference or a monotonic cast — linking collation fields to specific projection expressions. QED decides bag-semantic equivalence only, where Sort/Limit/Offset have no meaning (the prover models them as identities, and the DSL doesn't even expose a sort builder), so any encoding would either be vacuously "provable" while certifying nothing about ordering, or unable to express the collation↔projection linkage at all. The porter's stated reason was an infrastructure failure (LLM HTTP 400 context-length error, no attempt ever made) rather than an analysis — but the conclusion happens to be correct, since ordering semantics are a fundamental QED limitation per the reference, not a DSL gap closable via extend_dsl_file. ```
