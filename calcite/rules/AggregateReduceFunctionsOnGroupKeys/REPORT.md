# AggregateReduceFunctionsOnGroupKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsOnGroupKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's only semantic premise — that MAX/MIN/AVG/ANY_VALUE (or a `SqlConstantValueAggFunction`) applied to an argument that is constant within each group yields that constant (or its declared constant under a NULL-preserving CASE) — is an algebraic identity about specific aggregate functions, while QED models every aggregate call as an uninterpreted higher-order function of its input bag (qed.pdf §6.2), so for any non-vacuous encoding there exists an instantiation of the aggregate symbol under which before and after differ. No narrower special case survives: any variant provable without that premise must retain the aggregate call, reducing to a trivial identity rather than the call-elimination this rule is about, and the gap cannot be closed with `extend_dsl_file` since the uninterpreted treatment of aggregates lives in the QED prover itself, which is off-limits.
