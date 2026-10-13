# MergeNestedLimits

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 93-120
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness is a row-position identity — OFFSET drops the first k rows of a stream and LIMIT keeps the first n — but QED's bag semantics models Sort/Limit/Offset only as uninterpreted QOps with no ordering, index, or cardinality axioms (qed.pdf; the AGREE-verified LimitZeroToEmpty and FoldLimits precedents), so `before` and `after` are structurally distinct uninterpreted terms over X that SMT refutes for every instantiation, and no special case (not even idempotent `L(n,L(n,X))=L(n,X)` or an empty input) is derivable. The gap is in the trusted prover's semantics, not the DSL: `RelRN` has no Limit/Sort builder, and `extend_dsl_file` could at best serialize the `LogicalSort` node (sort/offset/limit JSON already exists in `JSONSerializer`) that QED still treats as opaque, so no encoding of the real nested-merge shape can prove. The transcript's isolated "provable" flag must therefore reflect a degenerate/structurally-identical stand-in encoding (consistent with its all-zero timings), not the nested-merge rule, so `UNSUPPORTED` is the correct permanent record.
