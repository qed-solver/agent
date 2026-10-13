# V2ScanPartitioningAndOrdering

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/V2ScanPartitioningAndOrdering.scala, lines 38-149
```

## Independent verifier review

**Verdict:** AGREE

V2ScanPartitioningAndOrdering is a pure physical-property annotation — both passes only rewrite plan-node attributes (`keyGroupedPartitioning`, `ordering`) via `d.copy` while the scan's row bag and schema are left untouched, so before and after are trivially bag-identical. QED is a bag-semantics decision procedure with no model of partitioning/ordering metadata (the JSON surface carries only rows plus `key`/`guaranteed` constraints, and even a `sort` node is defined to have no bag meaning), and the unmodifiable Rust prover has no semantics to interpret such attributes against, leaving the rule's key-resolvability and subset-of-output guards with nothing to check. Any RuleScript encoding therefore degenerates to the vacuous `scan ≡ scan` that verifies none of the rule's actual behavior — a fundamental QED limitation rather than a missing DSL builder the porter overlooked.
