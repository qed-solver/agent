# CalcToWindow

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectToWindowRule.java

Note: ProjectToWindowRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `CalcToWindowRule` variant (not ProjectToLogicalProjectAndWindowRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

CalcToWindow's before pattern necessarily contains a RexOver window expression and its after pattern necessarily produces a LogicalWindow node, and QED's bag-semantic translation has no Window operator — a stated limitation, since window values depend on ordering/frames within a partition and have no bag-semantic meaning. No uninterpreted-symbol workaround captures the rule's actual content: a window function's value for a row depends on the whole partition, not the row, so row-local uninterpreted functions misrepresent the before side; modeling the entire window as an uninterpreted relation symbol reduces the rewrite to a tautological projection reshuffle (and the prover's fixed JSON/Q-expression format has no window construct for a DSL extension to target, e.g. JSONSerializer throws on LogicalWindow); and the group-by substitute emits one row per partition instead of the one row per input row this cardinality-preserving decomposition requires. ```
