# OuterJoinToAntiJoin

**Status:** FAILED
**Source backend:** Apache Calcite
**Porter attempts used:** 150  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/OuterJoinToAntiJoinRule.java
```

## Independent verifier review

**Verdict:** DISAGREE

The porter's UNSUPPORTED is an artifact of the HTTP 400 context-length crash, not a limitation analysis — its own transcript ends with a complete, workable design, and none of QED's documented limits (no Sort/Limit/Window, no aggregate algebra, set-only INTERSECT/MINUS, no predicate inference, no list semantics) actually applies here. The LEFT-join case is within the core language: L = nullable scan, R = non-nullable scan via `scan("R", new RelType.VarType("R_Type", false), false)`, `before = left.join(LEFT, join_cond, right).filter(and(IS_NULL(rcol), g(lcol, rcol)))`, `after = left.join(ANTI, join_cond, right)` projected to `[lcol, NULL-literal]` and filtered by the same `g` over `(padded.field(0), padded.field(1))`, reusing the names `join_cond`/`g` so QED sees shared uninterpreted symbols; the null literal just needs a local record implementing `RexRN` whose `semantics()` returns `RuleBuilder.getRexBuilder().makeNullLiteral(type)`, which `JSONSerializer` already emits (`"operator":"NULL"`), and the per-column `"nullable"` flags in the schema show the prover models nullability/outer-join null padding — so `IS_NULL` on the null-generating side is decidable. The one genuine QED gap is Calcite's *alternative* safety condition for a nullable IS NULL column (join condition not TRUE when the field is NULL), which needs predicate inference between independent uninterpreted symbols; that only justifies submitting a `SCOPE: PARTIAL` special case assuming the IS NULL column is non-nullable, not UNSUPPORTED.
