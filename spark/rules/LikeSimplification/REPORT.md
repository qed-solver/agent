# LikeSimplification

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 878-984
```

## Independent verifier review

**Verdict:** AGREE

Every branch of LikeSimplification asserts an extensional equivalence between string pattern operators (LIKE 'a%' ≡ StartsWith, LIKE '%s%' ≡ Contains, LIKE 'a%b' ≡ StartsWith∧EndsWith with a length guard, LIKE 'x' ≡ =), but QED's SMT encoding translates all virtual types — including strings (see RelType's "All virtual types will be translated to integer for prover") — and treats each of these operators as an independent uninterpreted symbol with no axioms connecting them, so Filter(L,R) = Filter(S,R) is universally false for distinct symbols L, S and can only be "proven" by reusing one symbol name on both sides, which collapses the rule into a vacuous tautology that verifies nothing about the actual rewrite. This is a fundamental gap in the prover's theory (a backend operator's bespoke internal semantics over a string domain it does not model), not a missing Java-side builder — extend_dsl_file can only emit operator names the unmodifiable Rust prover already interprets as uninterpreted functions, and no amount of new builders can add the missing LIKE/StartsWith/Contains axioms. ```
