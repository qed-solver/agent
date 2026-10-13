# LimitZeroToEmpty

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_limit.rs, lines 1-89
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness (LIMIT with literal fetch 0 ⇒ empty relation, and skip-0/no-fetch ⇒ identity) rests entirely on the row-count/ordering semantics of LIMIT/OFFSET, but QED models Sort/Limit/Offset only as uninterpreted query operators (QOp) with no cardinality or ordering axioms, so `Limit(0, X)` is an unconstrained bag that SMT can instantiate as non-empty, making the equivalence with ∅ refutable for every input X. No non-trivial special case rescues it — even with an empty input (e.g. `Limit(0, Filter(false, X))`) there is no axiom linking the uninterpreted QOp's output to its input, so it remains unprovable. And closing it via `extend_dsl_file` is not an option: the DSL simply has no Limit/Sort builders, but adding one would serialize a LogicalSort that QED still treats as uninterpreted, since the limitation is in the trusted prover's bag-semantic model, not in the DSL's surface language. ```
