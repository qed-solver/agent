# SimplifyCasts

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 1247-1272
```

## Independent verifier review

**Verdict:** AGREE

Every non-trivial case of SimplifyCasts is sound only because of cast algebra — same-type cast being the identity, widening casts absorbing (cast₂∘cast₁ = cast₂), and value/null preservation — but in RuleScript a cast can only be expressed as an uninterpreted function symbol, and QED's semiring theory (qed.pdf, "Typing") attaches no axioms to such symbols, so no encoding or DSL extension can supply the missing identity/absorption/null-preservation facts (the fixed Rust prover is the arbiter and cannot be given them). The porter's empirical check is correctly designed: modeling `Cast(e, T)` as uninterpreted `cast_T: T → T` and asking for equivalence with the bare column asks QED to prove `∀x. cast_T(x) = x`, which SMT rightly refutes for an uninterpreted function — this is a genuine theory gap, not a name-mismatch or composition-encoding bug, and the rule's only purely structural case (the annotated-STRING guard) is not a rewrite at all, so no genuine provable special case remains. ```
