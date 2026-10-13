# ProjectReduceExpressions

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectReduceExpressionsRule` variant (not CalcReduceExpressionsRule, FilterReduceExpressionsRule, JoinReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

ProjectReduceExpressions' actual transformation is constant-folding an operator on constant arguments (`f(consts)` → literal) and redundant-cast removal, both of which require the *evaluation/type* semantics of those operators; QED models every such operator as an uninterpreted function over integer-valued symbols, so neither `f(consts) ≡ literal` nor `cast(x) ≡ x` is a theorem in that model, and no DSL extension (even a literal builder) can change that. The only simplifications QED *can* prove (pure Boolean connectives AND/OR/NOT with TRUE/FALSE literals) are a degenerate side-effect of `simplifyPreservingType` on a projected Boolean, not the rule's constant-reduction purpose, and are already exercised by the sibling Filter variant — so a real attempt at the rule's canonical case would just come back not-provable.
