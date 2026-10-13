# EliminateMax1Row

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/max1row.opt

EliminateMax1Row discards the Max1Row operator if its input is statically
guaranteed to have no more than one row. Removing the Max1Row operator is
important when decorrelating subqueries, as it interferes with ApplyJoin
pushdown when it's present.

Extracted from `max1row.opt` (which defines multiple rules — implement specifically `EliminateMax1Row`, not the other rules in that file):

```
# EliminateMax1Row discards the Max1Row operator if its input is statically
# guaranteed to have no more than one row. Removing the Max1Row operator is
# important when decorrelating subqueries, as it interferes with ApplyJoin
# pushdown when it's present.
[EliminateMax1Row, Normalize]
(Max1Row $input:* & (HasZeroOrOneRow $input))
=>
$input
```
```

## Independent verifier review

**Verdict:** AGREE

Max1Row is not a bag function — it is the identity only on inputs with at most one row and a runtime error otherwise — so it belongs to the row-count/Limit family that QED explicitly does not model, and it exists in neither the RelRN builders nor the prover's JSON vocabulary (the serializer's switch has no such node, and `extend_dsl_file` can only add builders over JSON the immutable Rust prover already understands, not new semantics). Furthermore, the rule is a conditional rewrite whose entire content is the `HasZeroOrOneRow` side condition, and `RRule` exposes only `before()`/`after()` with no assumption/guard channel, so the precondition must be satisfied structurally — but the core language's only "at most 1 row" shape is `.empty()` (exactly 0 rows; the `unique` scan flag is a distinct-key constraint, not a total row-count bound), which collapses the narrowest special case to a tautology that cannot even be written without the missing operator.
