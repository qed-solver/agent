# DistinctNoColumnsToLimitOne

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 48  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/replace_distinct_aggregate.rs, lines 89-100
```

## Independent verifier review

**Verdict:** AGREE

The rule's target is exactly `Limit { fetch: Some(1) }`, and QED does not model Limit/Offset/Sort (list/ordering semantics have no meaning in its bag semantics, with no axioms relating such an operator to its input), so the after pattern has no faithful encoding in the core language. The rewrite only holds because the input is zero-column (all rows identical), and the only at-most-one-row construct the bag core offers for a zero-column bag is the set-variant self-intersect — which is precisely how the before side (Distinct) itself is encoded — so any "encoding" either collapses to the trivial identity B∩B ≡ B∩B or is simply false if the placeholder has one or more columns (where DISTINCT can yield many rows while LIMIT 1 yields at most one), with no DSL constraint available to express the all-rows-equal premise. ```
