# SimplifyCaseConversionExpressions

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 46  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1284-1293
```

## Independent verifier review

**Verdict:** AGREE

The rule's only step is the pointwise idempotence laws Upper(Upper(x))⇒Upper(x) and Lower(Lower(x))⇒Lower(x), whose validity rests entirely on the internal algebra of the case-conversion functions. QED translates every named scalar operator — including both the generic symbols and the concrete SqlStdOperatorTable.UPPER/LOWER the porter tested — into an uninterpreted SMT function, and f(f(x))=f(x) is not a valid property of an arbitrary uninterpreted function, so the provable:false verdict is a genuine, complete (non-timeout) negative. No Java-side DSL extension can close this gap because the function axioms would have to live in the fixed Rust prover's translation, not in the pattern builders, making the UNSUPPORTED conclusion correct. ```
