# ExtractPythonUDFFromAggregate

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 27  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/python/ExtractPythonUDFs.scala, lines 39-85
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is moving a scalar Python UDF from inside Spark's Aggregate expression list (where Spark permits arbitrary expressions wrapping aggregate results) to a Project above the Aggregate, but in RuleScript's core language an Aggregate's output is exactly the group keys plus flat uninterpreted AggCalls over input rows — there is no position for a scalar expression over the aggregate's own result, and QED's JSON/Q-expression format has no corresponding node form, so a `extend_dsl_file` builder extension could not introduce it without changing the unmodifiable Rust prover. Any compilable before/after pair is therefore either the identical `Project(g, Aggregate(...))` on both sides (a vacuous identity, as the reviewer flagged) or unfaithful (e.g. placing g below the aggregate, which changes per-group vs per-row bag semantics). UNSUPPORTED is the correct verdict. ```
