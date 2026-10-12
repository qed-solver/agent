# PushFoldableIntoBranches

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 47  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 788-855
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness depends on the distributivity law f(ite(c,t,e)) ≡ ite(c,f(t),f(e)), which is only entailed when ite has its native conditional-selection semantics in the SMT encoding; QED serializes IF/CASE as a universally quantified uninterpreted function (as the porter's empirical run confirmed: complete_fragment=true, no timeout, provable=false), so no DSL-level encoding can recover the missing ite algebra without modifying the QED prover itself. ```
