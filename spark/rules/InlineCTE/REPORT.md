# InlineCTE

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/InlineCTE.scala, lines 58-339
```

## Independent verifier review

**Verdict:** AGREE

CTE inlining is a let-expansion rule (`WithCTE(B, Q(t))` ≡ `Q[B/t]`), but QED's Q-expression language has no let-binding or defined-name construct — the JSON vocabulary the prover consumes (scan, filter, project, join, correlate, aggregate, set ops, sort, values) contains no define/with/bind node — so the before pattern cannot link a CTE reference to its defining body. The only alternative encodings all fail: modeling the reference as an uninterpreted base table makes the identity false for arbitrary instantiations (QED correctly refutes it, since `t = B` is not expressible as a table constraint), and pre-identifying the reference with the body collapses the rule to a trivial identity; because the Rust prover is the untrusted-change-forbidden arbiter and has no `With` semantics, this is a fundamental QED limitation rather than a Java-DSL gap that `extend_dsl_file` could close. ```
