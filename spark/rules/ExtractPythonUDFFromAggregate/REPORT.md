# ExtractPythonUDFFromAggregate

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 39-85
```

## Independent verifier review

**Verdict:** AGREE

The rule's before-plan is a Spark `Aggregate` whose output column is an arbitrary "aggregate expression" `f(g, α)` mixing grouping keys and inner aggregate calls, but RuleScript's `Aggregate` only emits group keys plus flat `AggCall`s, so the only expressible before-plan is `Project(f(g, α), Aggregate(g; α))` — textually identical to the after-plan, i.e. a vacuous self-identity rather than the rule. Extending the DSL would not close this: any distinct representation of the composite output would be modeled by QED as an uninterpreted aggregate symbol, and QED has no definitional knowledge that Spark's aggregate expression `f(g, α)` means "f applied per-group on top of the inner aggregate α" (a backend operator's bespoke internal semantics), so a non-vacuous before/after pair would pit independent uninterpreted symbols against each other and never be provable. The rule's actual justification — a physical constraint that Python UDFs be evaluated only after aggregation — is likewise outside QED's bag semantics entirely.
