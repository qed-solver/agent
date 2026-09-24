# Match

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MatchRule.java
```

## Independent verifier review

**Verdict:** AGREE

Calcite's MatchRule is a pure self-copy of a `LogicalMatch` (SQL MATCH_RECOGNIZE) node, so the only faithful encoding needs a Match operator on both sides — but MATCH_RECOGNIZE is a sequence/ordering-dependent operator (partition/order keys, pattern matching over ordered rows, FIRST/LAST measures, strict start/end, interval) in the same fundamental class as the Sort/Window/Limit operators that QED explicitly cannot model with bag semantics, and the DSL offers no relation-level uninterpreted operator to stand in for it (uninterpreted symbols cover only scalar predicates/projections, tables, types, and join kinds, and QED's JSON format has no match node type to serialize to). Thus no before/after pair can even express the rule, and the only provable encoding — a content-free identity `R ⟹ R` over an uninterpreted relation — is a different, vacuous statement, not a port of this rule.
