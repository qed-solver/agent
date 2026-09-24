# AggregateMinMaxToLimit

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateMinMaxToLimitRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's target is a `ORDER BY c ASC/DESC LIMIT 1` scalar subquery, and QED has no ordering semantics — Sort/Limit carry no bag-semantic meaning (the serializer's `LogicalSort` case only shows the JSON *format* can carry such nodes; the prover still cannot decide them), so the "after" side cannot be faithfully encoded, and that gap lives in the prover's semantics, not in a missing builder `extend_dsl_file` could add. Independently, the rule's correctness is precisely the algebraic identity "MIN/MAX of a column = top-1 row of its ordering", but QED treats aggregate operators as uninterpreted beyond bag equality of their inputs, so it cannot connect an `aggOp(c)` call on one side with a sort/limit pipeline on the other. Since every instance of this rule — no matter how specialized (single column, unique key, filters) — requires exactly that identity to be established, no non-degenerate special case is provable, and the porter's UNSUPPORTED conclusion is correct. ```
