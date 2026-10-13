# HoistSelectSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 40  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistSelectSubquery extracts subqueries from a Select filter and joins them
with the Select input. This and other subquery hoisting patterns create a
single, top-level relational query with no nesting.

NOTE: Keep this ordered after the HoistSelectExists and HoistSelectNotExists
rules. This rule will hoist any existential subqueries using
LeftJoinApply, which is equivalent to, but not as efficient as, using
SemiJoinApply and AntiJoinApply.

This rule is marked as low priority for the same reason as HoistSelectExists.

Citations: [4]

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistSelectSubquery`, not the other rules in that file):

```
# HoistSelectSubquery extracts subqueries from a Select filter and joins them
# with the Select input. This and other subquery hoisting patterns create a
# single, top-level relational query with no nesting.
#
# NOTE: Keep this ordered after the HoistSelectExists and HoistSelectNotExists
#       rules. This rule will hoist any existential subqueries using
#       LeftJoinApply, which is equivalent to, but not as efficient as, using
#       SemiJoinApply and AntiJoinApply.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
#
# Citations: [4]
[HoistSelectSubquery, Normalize, LowPriority]
(Select
    $input:*
    $filters:[ ... $item:* & (HasHoistableSubquery $item) ... ]
)
=>
(HoistSelectSubquery $input $filters)
```
```

## Independent verifier review

**Verdict:** AGREE

Manually investigated by Claude. Same root cause as HoistProjectSubquery: read decorrelate_funcs.go's HoistSelectSubquery (and its doc example, 'WHERE (SELECT u FROM uv WHERE u=x LIMIT 1) IS NULL') — the hoisted subquery here is a general scalar subquery embedded in a filter comparison (not a boolean EXISTS), which gets hoisted into a LeftJoinApply/InnerJoinApply and its column referenced directly in place of the original subquery expression. QED's prover core has no handling for scalar subqueries ($SCALAR_QUERY) at all — confirmed by grep, zero hits anywhere in qed-prover's Rust source — so any such subquery used as a value falls through to a fully opaque HOp(op, args, rel, ty) with no defined relationship to 'the same relation's column, referenced directly once joined in.' There is no representational bridge for QED to reason across between those two forms — same fundamental gap as CorrelateUncollectOuter and HoistProjectSubquery, not a narrow-encoding opportunity like the EXISTS-boolean case (EliminateExistsProject/EliminateExistsZeroRows).
