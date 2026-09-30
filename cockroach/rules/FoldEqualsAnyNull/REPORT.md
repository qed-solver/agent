# FoldEqualsAnyNull

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 76  **Verification rounds used:** 5

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldEqualsAnyNull, converts a scalar ANY operation to NULL if the right-hand
side tuple is NULL, e.g. x = ANY(NULL::int[]). See #42562.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldEqualsAnyNull`, not the other rules in that file):

```
# FoldEqualsAnyNull, converts a scalar ANY operation to NULL if the right-hand
# side tuple is NULL, e.g. x = ANY(NULL::int[]). See #42562.
[FoldEqualsAnyNull, Normalize]
(AnyScalar * (Null) *)
=>
(Null (BoolType))
```
```

## Independent verifier review

**Verdict:** AGREE

Manually investigated by Claude (the automated run exhausted all 5 rounds on repeated context-length crashes, never reaching a real try_rule call). The rule needs: AnyScalar(x, NULL-array, cmp) == NULL, for ANY x — i.e. an uninterpreted operator whose result is forced NULL whenever one specific argument is NULL. Checked how QED's prover core models NULL (qed-prover/src/pipeline/shared.rs): is_null() is defined purely as equality against a fresh, uninterpreted NULL sentinel term (Logic::Eq(Op('NULL',[],ty), self)) — there is no general rule anywhere that propagates NULL through an uninterpreted function/predicate application based on its arguments. Only a small hardcoded set of operators get real interpreted semantics in relation.rs (COUNT, EXISTS, IS NULL/IS NOT NULL, boolean AND/OR/NOT); a custom 'AnyScalar' predicate, like any other generic RexRN.Pred, would be fully opaque with no null-propagation behavior at all, and the DSL has no CASE/conditional expression construct to manually encode 'if this arg is null, return null'. Unlike EliminateExistsProject/EliminateExistsZeroRows (where EXISTS turned out to have real hidden interpreted semantics reachable via a custom local RexRN calling raw Calcite APIs), there is no equivalent hidden machinery for null-strictness to tap into here — this is a genuine missing capability (general null-propagation reasoning for uninterpreted operators), not a DSL omission a self-contained trick can route around.
