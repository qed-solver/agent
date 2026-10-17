# CaseNoBranchesTrueToElse

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 87  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
```

## Independent verifier review

**Verdict:** AGREE

The rule's before side inherently contains a CASE scalar operator whose guard/branch-selection semantics are not in QED's modeled fragment — a CASE symbol in the pattern would be an uninterpreted operator unrelated to the else expression (independent-symbol entailment, a documented QED limitation), and since `JSONSerializer` only emits operator name strings, no `extend_dsl_file` change in the Java layer can supply CASE semantics that live in the immutable Rust prover. The porter's only provable encoding (attempt 1) drops the CASE and proves merely the propositional tautology (false∧a)∨(¬false∧b) ≡ b of the pre-expanded form, which is not a port of the CASE rewrite itself. This is exactly the documented category of "an operator whose specific internal semantics QED cannot see through as an uninterpreted function," so the UNSUPPORTED conclusion is correct.
