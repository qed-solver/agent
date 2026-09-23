# CalcSplit

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/CalcSplitRule.java
```

## Independent verifier review

**Verdict:** AGREE

CalcSplit merely de-fuses a Calc into Filter+Project, but RuleScript's core language has no fused Calc operator and a Calc is semantically just Project∘Filter, so the before and after sides are literally the same expression and the rewrite is a definitional tautology rather than a non-trivial equivalence. QED's Q-expression/JSON layer has no Calc node it can interpret — it only sees the decomposed Filter/Project/Join/Aggregate forms — so the fused operator's internal semantics are invisible to the prover and there is nothing beyond X==X for QED to decide; the porter's stated HTTP 400 context error is a red herring, the real blocker is the absent fused-operator semantics. ```
