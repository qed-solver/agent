# OptimizeRand

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 48  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeRand.scala, lines 33-63
```

## Independent verifier review

**Verdict:** AGREE

Every branch of OptimizeRand is only valid under the semantic axiom that rand() ∈ [0,1) (the folding) or under an equivalence between distinct comparison predicates (the operand swap), and QED supports neither: it models all function and predicate symbols as uninterpreted with no user-axiom channel, and the only side-information the rule format can carry is "guaranteed" constraints on base-table columns, which cannot attach to the output of a scalar function like rand(). Hence no narrowing faithful to the rule is provable — dropping the range assumption makes the folds unsound, and modeling rand as a constrained table column is a different rule — so this is a genuine QED limitation (the prover has no concept of function axioms) rather than a missing DSL builder that `extend_dsl_file` could close.
