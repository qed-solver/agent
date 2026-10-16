# AggregateReduceFunctions

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsRule.java
```

## Independent verifier review

**Verdict:** AGREE

Every non-degenerate branch of AggregateReduceFunctionsRule is an algebraic identity between distinct aggregate functions (AVG=SUM/COUNT, SUM vs SUM0/COUNT, the STDDEV/VAR/COVAR/REGR_SXX/SYY formulas over SUM/COUNT/REGR_COUNT, and MAX/MIN/AVG(x)=x when x is a group key), while QED treats each named aggregate as an uninterpreted symbol and has no axioms relating one aggregate's output to another's or to scalar arithmetic. The porter's empirical minimal test — the easiest branch, MAX(x) with x as the sole group key reduced to a call-free aggregate plus project — returned provable:false, which is exactly what the semantics predict, since no instantiation constraint forces an uninterpreted aggregate over a group-constant bag to return that constant. There is no narrower non-trivial special case to encode: any branch that actually rewrites an aggregate call differs in function names between before() and after(), so it is unprovable by construction, not by encoding. ```
