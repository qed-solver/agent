# FilterSortTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterSortTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

FilterSortTranspose's correctness rests on row-ordering semantics — the rule (which only fires on pure-order sorts, i.e. no limit/offset) is valid because filtering a sorted input preserves its collation order, and that ordering claim is exactly what QED does not model: it decides bag-semantic equivalence only, where Sort/collation carries no meaning, so both sides collapse to the same bag and the non-trivial content of the rule is inexpressible and unverifiable. This is a genuine fundamental limitation of the prover, not an encoding gap — even the porter's DSL extension adding a `sort` builder (which compiled regression-free) would at best yield a vacuous bag-identity proof, so UNSUPPORTED is the right conclusion. ```
