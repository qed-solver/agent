# ProjectReduceExpressions

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, JoinReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

ProjectReduceExpressions works by *evaluating* constant subtrees (e.g. 1+2→3, redundant CAST(x AS T)→x) using a RexExecutor, but QED models every such operator (arithmetic, CAST, CASE, etc.) as an uninterpreted symbol and only proves equivalences valid for *all* instantiations — it has no knowledge of any specific operator's algebra, so it cannot verify that f(const-args) folds to a literal. The porter's stated reason was actually an LLM context-length HTTP 400 (it never wrote the file or ran QED), not a technical judgment, but the UNSUPPORTED conclusion is nonetheless correct: constant folding / redundant-cast removal fundamentally depends on operator-specific internal semantics that QED cannot see through, so no non-trivial special case of this rule is provable. ```
