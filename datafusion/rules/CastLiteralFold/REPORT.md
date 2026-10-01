# CastLiteralFold

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 534-680
```

## Independent verifier review

**Verdict:** AGREE

The rule's essential content is cast value algebra — QED models any cast as an uninterpreted function (qed.pdf §6.2) with no cast identities, and since all virtual types flatten to integers and the only interpreted literals are boolean, even a "cast of a boolean literal" special case would assert a specific output of an uninterpreted function, which is by definition not universally provable. No `extend_dsl_file` fix can help, because a new `cast` builder would still serialize to an uninterpreted operator in the JSON that the fixed prover has no algebra for; additionally, the evaluator's surrounding machinery (CASE/COALESCE short-circuiting, deferral flags, plan-time error propagation) is execution-time plan-shape semantics with no bag-semantic meaning at all.
