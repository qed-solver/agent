# CalcToWindow

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToWindowRule.java

Note: ProjectToWindowRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `CalcToWindowRule` variant (not ProjectToLogicalProjectAndWindowRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

CalcToWindow’s validity depends on the actual semantics of `RexOver`/`LogicalWindow`—partitioning, ordering, and frames—to turn a scalar `OVER` expression into a per-row window relation. QED is stated to have no list/ordering semantics for `Window` (and its serializer/prover has no `LogicalWindow`/`RexOver` model), so a faithful before/after pair cannot be proved without treating the window as an opaque symbol that loses the rule’s content.
