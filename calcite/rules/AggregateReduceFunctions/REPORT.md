# AggregateReduceFunctions

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 34  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsRule.java
```

## Independent verifier review

**Verdict:** AGREE

AggregateReduceFunctions depends on aggregate algebraic identities (e.g. AVG(x) = SUM(x) / COUNT(x)) that QED cannot know, since it treats each aggregate function and scalar operator as an independent uninterpreted symbol with only bag-equality reasoning over a single aggregate's input. Thus no faithful encoding of the rule, including its simplest branch, can be proved.
