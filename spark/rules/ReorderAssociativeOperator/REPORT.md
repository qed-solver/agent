# ReorderAssociativeOperator

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 101  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 275-344
```

## Independent verifier review

**Verdict:** AGREE

RuleScript encodes scalar operators like `Add`/`Multiply` as uninterpreted function symbols in QED's SMT translation, carrying no algebraic axioms, so the prover cannot establish the associativity/commutativity, identity (`a+0=a`, `a*1=a`), zero-absorption, or literal-folding properties this rule's soundness rests on; even its narrowest subcase (`Add(Add(a,1),2) → Add(a,3)`) compares two syntactically distinct uninterpreted terms that QED can model as unequal, making it a genuine fundamental limitation rather than a fixable encoding gap.
