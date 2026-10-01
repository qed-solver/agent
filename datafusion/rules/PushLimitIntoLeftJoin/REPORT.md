# PushLimitIntoLeftJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 275-303
```

## Independent verifier review

**Verdict:** AGREE

The soundness of this rewrite rests entirely on limit/row-count semantics — that the outer LIMIT(skip, fetch) can only observe rows produced by at most skip+fetch left rows, a fact no bag-semantic model of QED can express — and QED (qed.pdf §6.2) treats Sort/Limit/Offset as uninterpreted higher-order QOps with no cardinality or ordering axioms, so any faithful encoding leaves before() and after() as structurally unrelated uninterpreted applications that SMT can never equate. This is a genuine prover-model gap rather than a missing builder: JSONSerializer already carries LogicalSort's offset/limit, so even an extend_dsl_file addition of a Limit builder would serialize fine but still be treated opaquely by the trusted prover. The lone "provable" result in the transcript came from a degenerate stand-in encoding (identical structure on both sides), not a proof of the actual rewrite, so the UNSUPPORTED conclusion is correct. ```
