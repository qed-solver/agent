# CombineSimpleEquivalence

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CombineSimpleEquivalenceRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite introduces a new spool table and then consumes it, but QED treats table scans as independent input bags with no let/view/spool binding tying a table's contents to the subplan that materializes it. Therefore the required materialization identity is outside QED's pure bag-semantics model, not just a missing builder or symbol-sharing bug.
