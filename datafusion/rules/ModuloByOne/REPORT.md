# ModuloByOne

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1210-1223
```

## Independent verifier review

**Verdict:** AGREE

The validity of `A % 1 = 0` rests entirely on the concrete algebra of the modulo operator (any integer divided by 1 has remainder 0), but QED's fixed SMT fragment contains only equality, uninterpreted functions, total order, natural-number addition, and ite — no modulo — so `%` can only be an uninterpreted symbol for which `∀A. mod(A,1)=0` is not valid. The porter's run after fixing the literal-type NPE returned a clean SMT refutation (`provable: false`, `complete_fragment: true`), confirming a genuine operator-semantics limitation rather than an encoding bug, and `extend_dsl_file` cannot help because the required modulo axioms would have to live in the immutable Rust prover. ```
