# AggregateReduceFunctions

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateReduceFunctionsRule.java
```

## Independent verifier review

**Verdict:** AGREE

Every reduction branch of this rule — AVG = SUM/COUNT, SUM → SUM0 wrapped in a CASE over COUNT, the STDDEV/VAR/COVAR/REGR expansions, and the MAX/MIN/AVG group-key shortcut — is an algebraic identity about specific aggregate functions, but QED models each aggregate call as an uninterpreted function of its input bag, so no encoding (general or narrowed) can establish before/after equality for arbitrary symbol instantiations; the FIRST_VALUE/LAST_VALUE branches additionally rest on row ordering, which QED does not model at all. Since the gap is in the prover's theory rather than the DSL, `extend_dsl_file` cannot close it, and any variant that avoids the aggregate algebra degenerates into a trivial identity rather than a real reduction. This is the same fundamental limitation already empirically confirmed by the rejected `AggregateReduceFunctionsOnGroupKeys` probe in the UnprovableRRuleInstances directory.
