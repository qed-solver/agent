# CalcSplit

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CalcSplitRule.java
```

## Independent verifier review

**Verdict:** AGREE

CalcSplitRule's correctness rests purely on the definitional identity that a fused Calc operator equals its Filter-then-Project decomposition; QED reasons only about relational-algebraic equivalences between plans built from its core operators, so a "fused" Calc is either (a) encoded as filter().project() in the DSL, making before() and after() structurally identical (a vacuous tautology), or (b) introduced as a new uninterpreted QOp for which QED has no axioms relating it to the Filter∘Project composition (unprovable). No DSL extension can close this gap because any serialization of the fused form either collapses to the split form (tautology) or remains opaque to the prover (uninterpretable), making this a genuine limitation of what a bag-semantic SMT-based equivalence checker can establish for operator-definition identities. ```
