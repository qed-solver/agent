# BitwiseAndByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1229-1235
```

## Independent verifier review

**Verdict:** AGREE

The DataFusion rule `x & 0 ⇒ 0` rests entirely on an algebraic identity of the BITWISE_AND operator, and any faithful RuleScript encoding (an uninterpreted projection symbol applied to a field and a `RexRN.Const` zero) turns it into the universal claim `∀R ∀x. f(x, 0) = 0`, which is false for an uninterpreted function and so is correctly refuted by QED's SMT decision procedure, not by an encoding bug. No `extend_dsl_file` change can help, since the DSL can already express the rule's shape and the missing axioms would have to live in the immutable Rust prover's SMT theory; nor does a non-vacuous narrower special case exist, because even the constant instance `f(c, 0) = 0` is not valid for an uninterpreted f (the only provable variant would be the vacuous `0 = 0`). This is exactly the documented limitation of an operator's bespoke internal semantics being opaque to QED, consistent with the porter's empirical `provable=false` and the analogous `FoldMultOne` (`x * 1 = x`) precedent. ```
