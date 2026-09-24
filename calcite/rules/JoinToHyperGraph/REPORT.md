# JoinToHyperGraph

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToHyperGraphRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's after-side is Calcite's bespoke `HyperGraph` node (N inputs, hyperedges as node-bitmaps, conflict rules, `notProjectInputs`), whose meaning is defined by planner-side enumeration machinery rather than any single bag-semantic expression, and QED's Q-expression/JSON model only knows scan/values/filter/project/join/correlate/group/set-ops/sort (see the switch in JSONSerializer.java) with no uninterpreted-relation symbol to stand in for a new operator — and the fixed Rust prover cannot be taught a "hypergraph" node, so `extend_dsl_file` is a dead end. The only QED-visible encoding (desugaring the after-side to a core-language expression, e.g. cross product + combined filter) proves a different rule — a join-flattening/reassociation identity, and only for INNER joins, since the rule's supported LEFT/FULL/SEMI/ANTI kinds genuinely lack tree-shape invariance — not the transformation into a HyperGraph. This is exactly the limitation category of a backend operator's bespoke internal semantics that QED cannot see through, so UNSUPPORTED is correct despite the porter's actual failure being a context-length API error. ```
