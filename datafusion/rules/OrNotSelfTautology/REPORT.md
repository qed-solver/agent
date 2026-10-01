# OrNotSelfTautology

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 108  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 978-985
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on A being non-nullable (in Kleene logic A OR NOT(A) = NULL when A = NULL), and the porter's encodings are corroborated by an internal consistency check: the Kleene-valid dual A AND NOT(A) → FALSE proved while the OR direction was rejected with a complete SMT run, which is the signature of QED modeling uninterpreted predicates as possibly-null rather than a symbol-sharing or encoding bug. QED's JSON contract carries nullability only per table column (verified in JSONSerializer: expression nodes serialize only an operator/operand/type with no nullability flag), so an uninterpreted predicate or projection can never be asserted non-null, and the only guaranteed non-null boolean — a non-nullable scan column used directly in the filter condition — is rejected by QED's filter translator, whose boolean-term grammar admits predicate calls/connectives/literals but panics on a column reference (verified for both a BOOLEAN-named and a generic-typed non-nullable column). This gap cannot be closed via extend_dsl_file, because it lies in the JSON contract (no per-expression nullability channel) and in the prover-side boolean grammar, neither of which is modifiable from the Java DSL — a genuine QED limitation, not a missed encoding. ```
