# PushLimitIntoTableScan

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 128-144
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity is purely a prefix-in-enumeration-order argument — the outer Limit(skip, fetch) can only consume the first skip+fetch rows of the scan's ordered output, and the tightened scan must return that same prefix rather than merely some smaller subset — and QED models only unordered bags, explicitly lacking any list/ordering semantics for Limit/Offset/Sort. A DSL extension cannot close the gap either: even though JSONSerializer can carry a sort node with offset/limit, the prover has no bag meaning for it, and the scan's fetch cap is a physical operator-internal property with no representation in QED's uninterpreted-table model at all (fetch is not serialized). No bag-semantic stand-in works — a filter predicate cannot cap cardinality or select an order-dependent prefix, and modeling the capped scan as an independent uninterpreted table would destroy the prefix relationship the proof depends on — so no encoding can be proved equivalent. ```
