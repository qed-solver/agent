# AggregateReduceFunctionsOnGroupKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsOnGroupKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The only substantive transformation in this rule is replacing aggregate calls (MAX/MIN/AVG/ANY_VALUE on a group key, or constant-value aggs like STDDEV_POP→0) with a reference to the group key itself — every branch is an algebraic identity about what a specific named aggregate returns, and QED encodes each aggregate as an uninterpreted bag-to-scalar function with no axioms relating its output to the group-constant input or to constants. The porter's minimal empirical test (MAX(x) over GROUP BY x → projection over the call-free aggregate) returned a clean, complete provable:false, which is exactly what the semantics predict for even the simplest branch, so the limitation is in the prover's model of aggregates, not in the DSL or in a symbol-sharing/encoding bug — and since every branch of the rule rests on this same unknown aggregate algebra, no narrower special case is provable either. ```
