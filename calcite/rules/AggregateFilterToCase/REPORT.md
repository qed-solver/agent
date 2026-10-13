# AggregateFilterToCase

**Status:** SKIPPED
**Source backend:** Apache Calcite

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToCaseRule.java
```

## Reason

The before-side is an aggregate call carrying a SQL `FILTER (WHERE ...)` clause. `RelRN.AggCall` has no filter field, `Aggregate.semantics()` builds calls without a filter argument, and `JSONSerializer` emits only operator/operand/distinct/ignoreNulls/type for aggregate calls, so the pattern the rule matches cannot reach the prover. Re-encoding the filter as a `Filter` below the aggregate changes the result (groups with no matching rows disappear, which FILTER preserves). Equating the filtered call with `agg(CASE WHEN c THEN x END)` would additionally need the aggregate's NULL-skipping algebra, but QED treats aggregates as uninterpreted functions of their input bag and the two operand bags differ by the CASE's NULLs. The sibling rules AggregateFilterToFilteredAggregate and AggregateCaseToFilter were skipped for the same reasons.
