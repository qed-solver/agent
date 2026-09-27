# InlineAnyValuesMultiCol

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 100  **Verification rounds used:** 0

## Source rule (as given to the porter)

```
Converts Any with Values input (multi-column) to AnyScalar.
```

## Independent verifier review

**Verdict:** AGREE (manual, re-investigated)

Corrected reasoning: the rule's LHS is `Any(Project(Values, tuple), scalar, cmp)` -- a *relational* ANY-subquery (Calcite's RexSubQuery.some(rel, nodes, ...)). Grepped QED's eval_logic (relation.rs): only EXISTS has real interpreted semantics for a subquery-shaped relation (Logic::squash(UExpr::sum(...))); SOME/IN/scalar all fall through to an opaque HOp with no defined boolean meaning, so the LHS itself isn't representable regardless of the RHS's encodability. (Note: an earlier version of this reasoning also claimed RuleScript has no literal-Values-with-constants construct -- that claim was WRONG and has been retracted; RuleBuilder.values(fields, literals...) plus JSONSerializer's existing LogicalValues case plus QED's real Values{schema,content} variant in relation.rs do support literal Values fully, as directly confirmed while proving the sibling rule MergeProjectWithValues. The literal-Values gap was never the real blocker here -- the missing relational-ANY interpreted semantics is.)

## QED prover result

```json
{}
```
