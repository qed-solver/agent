# ProjectToLogicalProjectAndWindow

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToWindowRule.java

Note: ProjectToWindowRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectToLogicalProjectAndWindowRule` variant (not CalcToWindowRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

This rule's payload is the identity between an inline `RexOver` (windowed aggregate) inside a `Project` and a separate `LogicalWindow` operator, but QED's Q-expression language contains no window operator (the serializer emits only scan/filter/project/join/correlate/group/union/intersect/except/sort), and window semantics require ordered-partition/frame (list) semantics plus the aggregate identity "per-partition aggregate joined back to each row equals the inline OVER value" — both things QED's bag semantics explicitly excludes, per its own limitations. Any encoding is forced to choose between (a) row-local uninterpreted functions, which makes the before-side window value an independent symbol the SMT solver cannot relate to the after-side group/join-back construction (a countermodel exists, as the porter observed), or (b) modeling both sides with the same group-by-then-join-back, which degenerates the rule into a vacuous identity that proves nothing about windows; even a DSL extension (e.g. scans with guaranteed constraints) cannot restore totality/cardinality for a window-relation symbol. Hence the porter's UNSUPPORTED verdict is correct, though its "group-by+join-back breaks cardinality" sub-point is imprecise (inner join-back on the group key is actually row-preserving) — the true blocker is the missing window/order semantics, which is a fundamental QED limitation, not a missed encoding.
