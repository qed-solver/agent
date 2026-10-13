# ReassignLambdaVariableID

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/objects.scala, lines 251-294
```

## Independent verifier review

**Verdict:** AGREE

ReassignLambdaVariableID is a pure alpha-rename/ID-reallocation of bound lambda variables inside scalar expressions — an expression-syntax invariant with zero effect on any relational plan's bag semantics, so the correct before/after encoding degenerates into either the identical pattern (a vacuous self-equality) or two independent uninterpreted symbols that QED by design cannot relate. QED's model has no lambda/binder construct (its only higher-order shape, subquery/correlate, binds an implicit fresh row variable that never appears in the input syntax), and since the missing named-binder node form is absent from the prover's own input format rather than merely lacking a Java builder, extend_dsl_file cannot close the gap. ```
