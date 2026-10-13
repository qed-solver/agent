# AggregateFilterToFilteredAggregate

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateFilterToFilteredAggregateRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's after-side is a per-call SQL `FILTER (WHERE P)`, which the pipeline cannot carry: `RelRN.AggCall` has no filter field and `Aggregate.semantics()` hardcodes the aggregateCall filter arg to `null`, and `JSONSerializer` emits only operator/operand/distinct/ignoreNulls/type for aggregate calls, so even a hand-built `LogicalAggregate` with a filter would be stripped before reaching the prover. qed.pdf §4.1 confirms QED's aggregate normal form is an uninterpreted function of its input bag with only the fixed non-NULL prefilter (COUNT(*) vs COUNT(col)), so the rule's core identity — `AGG FILTER(P) over R` = `AGG over Filter(P, R)` — is an aggregate-algebra fact QED fundamentally cannot express or prove. No re-encoding in the core language closes the gap (encoding the after-side as the before-side is a trivial self-equality, and a `CASE`-based surrogate would require QED to know its aggregate's algebra), so UNSUPPORTED is the correct conclusion, not a missed encoding. ```
