# StructCastSameFieldCountFold

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 534-680
```

## Independent verifier review

**Verdict:** AGREE

This "rule" is not a relational-algebra rewrite but a constant-folding step in DataFusion's const evaluator: CAST(struct_literal AS struct_type) is "simplified" by actually *evaluating* the cast on a 1-row dummy batch (with field-count/name-overlap/non-emptiness guards), so its correctness rests entirely on the backend's bespoke struct-cast semantics — and struct types with named fields aren't even expressible in RuleScript's type system, since the prover erases all virtual types to plain integers. In that type-erased world the rewrite goal reduces to proving f(c) = c' for an uninterpreted cast function f (with c, c' both serialized as the literal 0), which the SMT solver always refutes with a countermodel like f(0) ≠ 0, and no `extend_dsl_file` change can help because the QED prover is fixed and assigns no axioms to new operators — this is exactly the "operator whose specific internal semantics QED cannot see through" limitation, so the porter's UNSUPPORTED conclusion is correct.
