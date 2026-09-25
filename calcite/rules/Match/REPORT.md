# Match

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 7  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MatchRule.java
```

## Independent verifier review

**Verdict:** AGREE

This rule is a pure identity transformation — `MatchRule` rebuilds a `LogicalMatch` field-for-field identical — so any faithful encoding must place a MATCH_RECOGNIZE node in both `before()` and `after()`, and QED has no semantics for that operator: it decides only bag-semantic equivalence with uninterpreted scalar functions, explicitly has no list/ordering model (the same reason Sort/Window/Sample are out), and the JSON bridge (`JSONSerializer.java`) has no `LogicalMatch` case, throwing in its default arm, so the unmodifiable Rust prover has no node it could interpret even if a `RelRN.match` builder and serializer case were added. The only degenerate workaround (e.g. introducing a fresh uninterpreted scan for the match output on both sides) would no longer express this rule — it would reduce to a vacuous `Scan = Scan` that says nothing about the Match operator's pattern, partitioning, or measures — so the porter's UNSUPPORTED conclusion and its stated fundamental limitation are correct.
