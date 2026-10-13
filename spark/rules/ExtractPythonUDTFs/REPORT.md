# ExtractPythonUDTFs

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 29  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 401-420
```

## Independent verifier review

**Verdict:** AGREE

The rule is a pure execution-strategy specialization: before (Generate(PythonUDTF,…)) and after (BatchEvalPythonUDTF/ArrowEvalPythonUDTF) carry the identical child, requiredChildOutput, and generatorOutput, so the only real proof obligation — that the two physical Python-eval nodes compute the same rows as the logical Generate — is a claim about backend operators' bespoke internal semantics that QED cannot see through as uninterpreted functions. Additionally, a UDTF generate is a per-row table/list-producing operator, and neither the core language nor QED's JSON format (scan/filter/project/join/correlate/aggregate/set-ops/sort only) has any operator to model it, so any expressible encoding would reduce both sides to the same shape and prove only a tautology, not the rule's actual content. ```
