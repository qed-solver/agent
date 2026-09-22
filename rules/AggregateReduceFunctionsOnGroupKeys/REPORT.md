# AggregateReduceFunctionsOnGroupKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsOnGroupKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

Every reduction branch of this rule (MAX/MIN/AVG/ANY_VALUE applied to a group key becoming the key reference, or a SqlConstantValueAggFunction becoming its declared constant, with NULL-preserving CASE) rests on the algebra of the specific aggregate function, whereas QED models every aggregate call as an uninterpreted function of its input bag — so the before/after equality fails for some instantiation of the aggregate symbol for any encoding, and the porter's probe (not provable, no timeout, complete) was the correct experiment, not a symbol-mismatch bug to diagnose further. No non-trivial special case survives: any provably-correct variant would have to omit the call elimination entirely, which is a trivial identity rather than the rule. This is a genuine QED limitation (no aggregate algebra beyond bag equality of inputs, and QED itself is unmodifiable), not a missing DSL capability, so extend_dsl_file cannot close the gap. ```
