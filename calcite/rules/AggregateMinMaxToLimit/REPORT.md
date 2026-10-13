# AggregateMinMaxToLimit

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateMinMaxToLimitRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's after side (scalar subqueries of the form SELECT c FROM t WHERE c IS NOT NULL ORDER BY c ASC/DESC LIMIT 1 over a one-row VALUES) is only equivalent to MIN/MAX if the prover knows that the first row of an ordered bag is its minimum/maximum — but QED assigns no bag-semantic meaning to Sort/Limit (no ordering axioms exist), and MIN/MAX are uninterpreted higher-order aggregates it can only compare by bag equality of their inputs, so it cannot relate them to any ordering at all. Even if the DSL were extended with sort/limit/scalar-subquery builders (JSONSerializer already serializes LogicalSort and RexSubQuery, so that gap is closable), the semantic gap is fundamental: a faithful after side must carry ordering, and no ordering statement is provable without order semantics. Hence no provable special case exists, and the porter's UNSUPPORTED conclusion is correct (its one minor imprecision — saying QED "models" Sort/Limit as uninterpreted operators rather than leaving them semantically unmodeled — does not change the verdict). ```
