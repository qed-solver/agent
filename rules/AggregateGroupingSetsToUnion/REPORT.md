# AggregateGroupingSetsToUnion

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateGroupingSetsToUnionRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's left-hand side is a GROUPING SETS aggregate, but QED's language has no such operator: `RelRN.Aggregate` builds only simple group-key aggregates (`RelBuilder.groupKey` + single group set), and the JSON contract the Rust prover consumes serializes a `group` node with only one "keys" list, so the prover has no interpretation for the operator whose expansion identity is the entire content of this rule. Encoding the "before" as a simple aggregate over the full group set instead would assert a false equivalence (one aggregate vs. a UNION ALL of sub-aggregates over its subsets, not bag-equal under QED's uninterpreted-aggregate model), and `extend_dsl_file` cannot help since it only touches the Java builders/serializer, not the unmodifiable prover's semantics. (A secondary, fixable gap is that `RexRN` has no NULL-literal builder for the padding columns — but the missing grouping-sets operator is the real blocker, so a fresh attempt would only confirm non-provability.) ```
