# Match

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 28  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MatchRule.java
```

## Independent verifier review

**Verdict:** AGREE

Calcite's `LogicalMatch` is an ordering/frame-dependent CEP operator (pattern matching over an ordered stream with measures/interval), so it has no bag-semantic meaning — exactly the unmodeled class QED lists alongside Window/Sort. The Q-expression/JSON format has no `match` construct (JSONSerializer hits its `default -> throw` on `LogicalMatch`), and since QED interprets a fixed relational-operator set with no uninterpreted-relational-operator slot, there is no way to express a MATCH node in `before()`/`after()` — and we cannot modify the Rust prover to add one. MatchRule is a pure identity placeholder (rebuilds the same LogicalMatch from the same fields), so the rule's only content is `MATCH → MATCH`, which is neither expressible nor non-trivial here; a `scan → scan`-style stand-in would mis-model the rule rather than encode it. ```
