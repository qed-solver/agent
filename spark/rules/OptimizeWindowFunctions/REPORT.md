# OptimizeWindowFunctions

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2058-2069
```

## Independent verifier review

**Verdict:** AGREE

This is a Window rewrite whose soundness rests on ordering/frame semantics (the "first row" of a non-empty-ORDER BY ROWS frame), but QED decides only bag-semantic equivalence — Window has no bag-semantic meaning, is absent from both the core language and JSONSerializer, and the Rust prover has no model for it, so the gap is a fundamental limitation that extend_dsl_file cannot close. Independently, `First` and `NthValue(_,1)` are two distinct uninterpreted aggregate symbols with no axioms or identity relating them, so QED cannot prove their outputs agree even over an identical input bag. Thus the only "provable" encoding would collapse the two to one symbol, reducing the rule to a no-op — no genuine non-trivial fragment exists, so UNSUPPORTED is correct. ```
