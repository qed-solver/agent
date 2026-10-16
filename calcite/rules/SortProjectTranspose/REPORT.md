# SortProjectTranspose

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SortProjectTransposeRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness is purely an order-preservation claim: a permutation (plus monotonicity-preserving casts) over a stream sorted by collation C yields the same row ordering — and, with offset/fetch, the same top-k row *selection* — as sorting the input by the remapped collation; the permutation guard and the monotonic-cast check exist only to protect this list-semantic invariant. QED verifies only bag equivalence, where Sort/Offset/Fetch carry no semantic content (sort reduces to identity), so any encoding of before/after collapses to the vacuous tautology Project(perm, R) ≡ Project(perm, R) — a statement that holds even for non-permutation projects and thus cannot certify the collation remapping the rule actually depends on. This is the documented fundamental limitation on row-ordering/top-k reasoning, not a missing DSL operator, so `extend_dsl_file` could not close the gap and the UNSUPPORTED verdict stands.
