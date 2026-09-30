# BitwiseShiftByZeroNoop

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 32  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1452-1457
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests entirely on the operator-specific algebraic law x >> 0 = x (including its null behavior), but in RuleScript the shift can only be introduced as an uninterpreted scalar function symbol — the core language has no numeric literal or interpreted bit-arithmetic to represent the zero operand in any more constrained way — and QED's logic (equality, order, ite over uninterpreted functions, with arithmetic reserved for bag multiplicity) contains no axiom connecting a shift-by-zero to its operand, so the identity is not entailed under any instantiation no matter how the encoding is shaped. The porter's minimal empirical test (provable=false with the only difference being the uninterpreted shift symbol) is therefore the predicted outcome of a genuine limitation of the fixed prover's theory, not a symbol-sharing or function-composition mistake, and no `extend_dsl_file` change can close it since the missing shift/zero axioms live in the unverifiable Rust prover, not in a missing DSL builder.
