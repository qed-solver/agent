# EliminateDistinctOnValues

**Status:** SKIPPED
**Source backend:** CockroachDB

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt
```

## Reason

The rule removes a DistinctOn whose input is a constant Values that is already distinct on the grouping columns. The porter's encoding proved, but its two sides serialize to the identical plan: Calcite's RelBuilder.aggregate drops an aggregate whenever the input is known to be unique (including a Values with distinct rows and a Values under a Filter), so `before()` is already the bare input when it reaches the prover and QED compares a plan with itself. The same holds with the Values wrapped in a Filter over an uninterpreted predicate. The porter's proof also hit a separate merge problem, but that is not the cause. The rule's real content, converting an aggregate with FirstAgg/ConstAgg columns into a projection, is outside what QED can relate, since ordinary aggregates have no derivation of 'aggregate over a singleton group equals its argument', and the empty-aggregation case is the already-proved EliminateDistinct.
