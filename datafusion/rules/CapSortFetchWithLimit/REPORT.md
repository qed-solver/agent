# CapSortFetchWithLimit

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 161-185
```

## Independent verifier review

**Verdict:** AGREE

QED works in pure bag semantics and, per qed.pdf §6.2, explicitly has no model for the ordering semantics of Sort/Limit/Offset ("does not support queries that make use of the ordering semantics of Limit, Offset, or Order By"), yet this rule's entire content is row-position semantics: Sort's fetch capping the number of rows emitted (top-K), Limit(0, n) being redundant once its input is bounded to ≤ n rows, and the min-algebra on the fetch arguments — none of which are properties of the bag of rows. Under QED's uninterpreted treatment, the before/after sides differ only in the structure and fetch arguments of those uninterpreted higher-order operators over the same input, so the SMT solver has no algebraic relation to bridge and no narrower special case (e.g. skip=0 with an unchanged fetch cap) removes the dependency on the missing ordering semantics. Note the DSL also lacks a Sort/Limit builder, but that is not the blocker — extending `RelRN` could at best expose operators the prover itself deliberately gives no bag-semantic meaning to, so the UNSUPPORTED conclusion is correct. ```
