# SimplifyRegexToLike

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1710-1723
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on string/glob semantics (the pattern "%" matches exactly the non-null strings, and nothing else), but QED has no string theory and encodes LIKE as an uninterpreted predicate symbol, so in the SMT model like(x,"%") may be false on a non-null input or true on NULL — hence neither the non-nullable case (⊨ like(x,"%")) nor the nullable case (like(x,"%") ⟺ x IS NOT NULL) is entailed, matching the documented limitation that QED cannot see through an operator's specific internal semantics as an uninterpreted function. The porter correctly noted this is not a fixable DSL gap: any new Java-side operator still serializes through the same name-based uninterpreted path in JSONSerializer, and the only lever available in RuleScript (table "guaranteed" constraints) would require assuming like(x,"%") ≡ isNotNull(x) as a premise — i.e., assuming the rule itself, which is circular and proves nothing. UNSUPPORTED is the right conclusion. ```
