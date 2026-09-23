# CombineSimpleEquivalence

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CombineSimpleEquivalenceRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's essence is to factor a shared sub-plan through a materializing `Spool` — a producer writes the sub-plan's rows to a temp table and a separate consumer `LogicalTableScan` in a different branch reads them back — which is a stateful, cross-branch producer→consumer data dependency that QED cannot express, since it models every relation as a pure bag function of independent, uninterpreted table scans (to QED the consumer scan is just an arbitrary table, not the producer's output). The rule's trigger is also structural `RelDigest` common-sub-expression detection, a syntactic notion QED never evaluates, so no `before`/`after` encoding could be shown bag-equivalent.
