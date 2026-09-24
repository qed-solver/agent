# ProjectToLogicalProjectAndWindow

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToWindowRule.java

Note: ProjectToWindowRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectToLogicalProjectAndWindowRule` variant (not CalcToWindowRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire semantic content is that a windowed aggregate (RexOver) evaluated inline inside a Project yields the same per-row values as the same aggregate evaluated by a separate LogicalWindow node over the same input — i.e., partition/ordering-dependent window semantics, which QED explicitly does not model (Window has no bag-semantic meaning, and QED knows nothing about an aggregate's algebra beyond bag equality of its input). Any encoding in the core language either desugars the window on both sides (self-join back to a group-by), making before and after the identical tree so the proof is vacuous, or relates an opaque uninterpreted window function to a group-by/join construction, which QED cannot derive for uninterpreted symbols. This is a fundamental limitation on the prover side (no window semantics in the QED backend), not a missing DSL builder, so extend_dsl_file cannot close the gap and no non-trivial narrower special case of the rule's content is provable. ```
