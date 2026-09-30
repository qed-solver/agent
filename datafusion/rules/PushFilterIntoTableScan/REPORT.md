# PushFilterIntoTableScan

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 1224-1301
```

## Independent verifier review

**Verdict:** AGREE

QED models every table scan as an uninterpreted bag of a table symbol, and the JSON format's only scan-level mechanism (the "guaranteed" check constraint) asserts a pre-existing property of the table's rows (Filter(C,T)≡T), whereas the Exact path of this rule requires the missing axiom that a *new scan instance's* output equals Filter(P,T) over the unfiltered table — a bag equality defined by the provider's execution-time TableProviderFilterPushDown::Exact contract, which the prover has no operator or axiom for and cannot see through uninterpreted symbols. The Inexact path is the logical no-op Filter(P,Scan)≡Filter(P,Scan) (scan.filters is planner metadata invisible to bag semantics), and the only would-be encoding of the Exact path — reusing the same table symbol with a P-guarantee so before=Filter(P,T) and after=T — smuggles the very equivalence the rule is supposed to establish in as a table precondition, proving check-constraint filter elimination rather than pushdown. This is the same fundamental limitation already adjudicated for Calcite's FilterTableScan (verifier AGREE), so the UNSUPPORTED call is correct. ```
