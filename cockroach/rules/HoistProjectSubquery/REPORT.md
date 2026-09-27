# HoistProjectSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 100  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistProjectSubquery extracts subqueries from a projections list and joins
them with the Project input. This and other subquery hoisting patterns create
a single, top-level relational query with no nesting.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistProjectSubquery`, not the other rules in that file):

```
# HoistProjectSubquery extracts subqueries from a projections list and joins
# them with the Project input. This and other subquery hoisting patterns create
# a single, top-level relational query with no nesting.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistProjectSubquery, Normalize, LowPriority]
(Project
    $input:*
    $projections:[
        ...
        $item:* & (HasHoistableSubquery $item)
        ...
    ]
    $passthrough:*
)
=>
(HoistProjectSubquery $input $projections $passthrough)
```
```

## Independent verifier review

**Verdict:** AGREE (manual)

Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_rule call). Read the actual implementation (decorrelate_funcs.go's HoistProjectSubquery + subqueryHoister): it hoists a scalar-valued correlated subquery used inside a Project's projection list (e.g. (SELECT max(u) FROM uv WHERE u=x) AS m) out into an INNER or LEFT JOIN LATERAL, choosing the join type based on the subquery's cardinality guarantees, then references the join's own output column in place of the original subquery expression. Checked whether the EXISTS trick used for EliminateExistsProject/EliminateExistsZeroRows extends here: Calcite does provide RexSubQuery.scalar(RelNode), but grepping the entire qed-prover core turns up zero handling for it ($SCALAR_QUERY) — unlike EXISTS (which has a real interpreted match arm in relation.rs's eval_logic), a scalar subquery used as a value falls through to a fully opaque HOp(op, args, rel, ty) with no defined relationship between 'this subquery's value, expressed as an opaque function of its embedded relation' and 'the same relation's column, referenced directly once joined in via Correlate.' There is no representational bridge connecting those two forms for QED to reason across — the same class of fundamental gap as CorrelateUncollectOuter (a missing operator concept), not a narrow-encoding opportunity like the EXISTS-boolean case.
