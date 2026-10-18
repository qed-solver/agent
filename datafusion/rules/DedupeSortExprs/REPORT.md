# DedupeSortExprs

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 11  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_duplicated_expr.rs, lines 68-114
```

## Independent verifier review

**Verdict:** AGREE

DedupeSortExprs is a pure ORDER BY optimization — deduping sort keys and FD-pruning never change the multiset of output rows, only their sequence — and QED is a bag-semantic decision procedure that explicitly does not model Sort/Order-By ordering semantics (qed.pdf §6.2), so any encoding of before/after collapses both sides to bag(input), a vacuous tautology that proves nothing about order preservation; the FD-pruning branch additionally rests on content-dependent functional dependencies that cannot be stated for uninterpreted table symbols, and this is a genuine prover limitation, not a missing DSL builder, so extend_dsl_file could not close the gap. ```
