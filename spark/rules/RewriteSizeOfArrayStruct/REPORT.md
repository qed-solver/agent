# RewriteSizeOfArrayStruct

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 89  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RewriteSizeOfArrayStruct.scala, lines 44-96
```

## Independent verifier review

**Verdict:** AGREE

The rule rewrites `size(a)` into `size(GetArrayStructFields(a))`, and its soundness rests entirely on the operator-specific law that struct-field extraction preserves array length and null-ness — the internal list/element semantics of Spark's `Size`/`GetArrayStructFields`, which QED by design does not model. In RuleScript both operators can only be named as uninterpreted projection symbols, and QED correctly cannot derive `size(f(x)) = size(x)` for an uninterpreted `f`; the only premise channel (row-constraints "guaranteed" on a scan) would have to assert that very equality, reducing the proof to trivial equality substitution and making the verdict vacuous. This is a fundamental prover limitation (opaque uninterpreted operator internals, no list semantics), not a missing DSL capability that `extend_dsl_file` could close, so the porter's UNSUPPORTED conclusion is correct. ```
