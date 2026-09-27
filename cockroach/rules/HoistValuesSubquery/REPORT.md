# HoistValuesSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 2  **Verification rounds used:** 0

## Source rule (as given to the porter)

```
Extracts subqueries from row tuples and joins them with the Values operator.
```

## Independent verifier review

**Verdict:** AGREE (manual)

Same representational gap as HoistProjectSubquery/HoistSelectSubquery: the rule generically hoists a correlated Subquery, Exists, or Any expression out of a scalar position (here, a VALUES row tuple) into a join; the rule's own canonical example is a *scalar* subquery used as a value (VALUES ((SELECT u FROM uv WHERE u=x LIMIT 1))). QED's prover core has no interpreted semantics for a scalar subquery's value (Calcite's $SCALAR_QUERY / RexSubQuery.scalar) -- it falls through to a fully opaque HOp with no defined relationship to 'the same column, once the subquery's relation is joined in.' Only EXISTS has real interpreted semantics in relation.rs (eval_logic's Logic::squash(UExpr::sum(...)) case), and this rule's row-tuple-value use case is not an EXISTS/boolean position, so the Exists-record trick doesn't apply here.

## QED prover result

```json
{}
```
