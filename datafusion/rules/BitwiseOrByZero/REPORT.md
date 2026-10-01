# BitwiseOrByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1303-1308
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is the semantic identity x | 0 = x of the specific bitwise-or operation, but RuleScript can only introduce `|` as an uninterpreted scalar symbol, and QED's fixed oracle theory (equality/uninterpreted functions, total order, natural-number addition, ite) contains no bitwise-arithmetic axioms; since a rule is just a before/after pattern pair with no assumption mechanism and `extend_dsl_file` cannot alter the unmodifiable Rust prover, no encoding can supply that identity — and bitwise OR is not even definable from the available addition/order arithmetic, so no faithful surrogate encoding exists. A real `try_rule` attempt would therefore only return not-provable (the solver can countermodel `|` as any function, e.g. a constant one), so the porter's UNSUPPORTED conclusion, though reached without a live attempt, is correct.
