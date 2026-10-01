# ModuloByOne

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 12  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1210-1223
```

## Independent verifier review

**Verdict:** AGREE

QED models every scalar operator (including `Modulo`) as an uninterpreted function symbol over uninterpreted types, and the DSL exposes only the boolean literals true/false — no way to name the numeric constants 1 and 0 that the rule requires. The identity ∀a. a mod 1 = 0 is not valid for arbitrary instantiations of an uninterpreted modulo symbol and arbitrary constant symbols, so no SMT solver without an arithmetic theory can prove it, and QED's Rust prover has none by design. The gap is in the prover's theory, not in missing DSL surface syntax, so `extend_dsl_file` cannot close it and UNSUPPORTED is the correct conclusion. ```
