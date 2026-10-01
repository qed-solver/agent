# PushLimitIntoRightJoin

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 275-303
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on row-count/ordering semantics — that in a right join each preserved right-input row appears in the join output (in input order), so the first skip+fetch output rows can only come from the first fetch right rows — and QED models Sort/Limit/Offset as uninterpreted QOps with no bag-semantic meaning, no cardinality, and no ordering axioms (qed.pdf §6.2; the core language has no Limit builder at all). Any faithful encoding leaves before() and after() as structurally distinct applications of an opaque uninterpreted operator over different join trees, which the SMT solver can never equate, and even a Limit builder added via extend_dsl_file would only serialize into that same opaque QOp without granting the prover any row-count reasoning. This is a genuine prover-model gap, not a missing DSL surface or an untried encoding — the same fundamental limitation for which the sibling PushLimitIntoLeftJoin was already ruled UNSUPPORTED.
