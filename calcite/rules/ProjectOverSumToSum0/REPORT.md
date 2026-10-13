# ProjectOverSumToSum0

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 18  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectOverSumToSum0Rule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's subject — `SUM(x) OVER <frame>` vs `SUM0(x) OVER <frame>` inside a project — is inherently a windowed aggregate, and QED's theory has no window/frame/ordering semantics at all (no window node in its Q-expression format, and a Calcite `RexOver` would mis-serialize as a plain scalar `RexCall` with partition/order/frame silently dropped), so the rule's shape cannot be faithfully encoded, not merely narrowed. The residual content — `SUM` ≡ `SUM0` — is an aggregate null/empty-handling algebra identity QED cannot know (aggregates are uninterpreted beyond input bag equality, and the identity is in fact false over a general bag), which the porter's frameless probe empirically confirmed with a clean, complete `provable: false` rather than a symbol-sharing bug. These are fundamental QED limitations (no list/ordering semantics for Window; no aggregate-function algebra), not a missing DSL builder that `extend_dsl_file` could close.
