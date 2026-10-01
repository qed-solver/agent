# BitwiseAndByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1229-1235
```

## Independent verifier review

**Verdict:** AGREE

The rule's only non-trivial content is the operator-specific numeric law `A & 0 = 0`, but RuleScript can introduce `&` solely as an uninterpreted scalar symbol — the core language has no integer literals or interpreted bit-arithmetic, so the zero operand and its zero-ness cannot be expressed in any more constrained way — and QED's fixed theory over such symbols (equality, uninterpreted functions, ite, ordering, with arithmetic reserved for bag multiplicity) entails no axiom of the form `f(x, c) = c` for an arbitrary constant symbol `c`, so the identity is not valid under all instantiations no matter how the encoding is shaped. This is a genuine limitation of the unmodifiable prover's theory, not a missing DSL builder, so `extend_dsl_file` cannot close the gap and the porter's empirically confirmed `provable=false` is the predicted outcome.
