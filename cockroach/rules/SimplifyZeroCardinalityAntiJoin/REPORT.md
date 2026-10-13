# SimplifyZeroCardinalityAntiJoin

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 113  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

SimplifyZeroCardinalityAntiJoin converts an AntiJoin operator to an empty
Values when it's known that the right input never returns zero rows, and
there is no join condition.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyZeroCardinalityAntiJoin`, not the other rules in that file):

```
# SimplifyZeroCardinalityAntiJoin converts an AntiJoin operator to an empty
# Values when it's known that the right input never returns zero rows, and
# there is no join condition.
[SimplifyZeroCardinalityAntiJoin, Normalize]
(AntiJoin | AntiJoinApply
    $left:*
    $right:* & ^(CanHaveZeroRows $right)
    []
)
=>
(ConstructEmptyValues (OutputCols $left))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule is sound only under the optgen side condition `^(CanHaveZeroRows $right)` — i.e. the right input is guaranteed non-empty — which is a semantic property of a subtree, not a plan shape, and RuleScript has no mechanism to assert or assume such a property: uninterpreted scans range over all bags including the empty one, the `unique`/key flags don't imply non-emptiness, and there is no non-empty `VALUES` or any operator whose output QED can certify to have ≥ 1 row (a global aggregate's output is treated opaquely beyond input-bag equality, as the porter confirmed empirically with `provable: false`). Dropping the condition makes the rewrite outright unsound — an anti-join with an empty right and no condition returns the entire left input rather than ∅ — so no side-condition-free encoding can be proved, and extending the DSL wouldn't help since the JSON table schema carries only per-row `guaranteed` predicates and key sets, not a bag-cardinality constraint the (unmodifiable) QED prover would honor. ```
