# PushFilterIntoTableScan

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 1224-1301
```

## Independent verifier review

**Verdict:** AGREE

The only branch of this rule with bag-semantic content — dropping an `Exact`-pushed conjunct from the Filter above a TableScan — is sound solely by the provider's execution-time contract that a scan configured with predicate P returns exactly the rows of Filter(P, T); QED models scans as uninterpreted bag relations whose only axioms are key uniqueness and check constraints on the table's own rows, so no operator or axiom can relate a filter-pushing scan to the filtered original (distinct scan symbols are independent, and same-name scans resolve to one symbol), and the sole available encoding — pre-asserting P as a table precondition — proves constraint-based filter elimination rather than pushdown. The Inexact/volatile/subquery branches introduce no bag-semantic difference, since `scan.filters` is planner metadata invisible to the prover, so no narrower special case carries the rule's actual content. ```
