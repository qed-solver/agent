# BitwiseShiftByZeroNoop

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 35  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1452-1457
```

## Independent verifier review

**Verdict:** AGREE

The rule is a value-level scalar identity — shift(A, 0) = A — which in RuleScript can only be encoded as Project(f(A, 0)) ⟹ Project(A) where the shift is an uninterpreted projection operator, and QED's SMT encoding asks whether ∀a. f(a,0)=a holds for an uninterpreted f, which is false without an identity axiom. The porter verified the encoding compiles and serializes cleanly (so it's not a DSL gap — the JSON format carries operators only by name, with no channel to attach axioms, and the prover is the fixed arbiter with no shift semantics), making this precisely the limitation where a rule's correctness rests on an operator's specific internal semantics that QED cannot see through as an uninterpreted function. Any "provability trick" (e.g. naming the operator something QED has axioms for) would prove a different rule, not the shift identity, so no honest encoding — full or special-case — exists. ```
