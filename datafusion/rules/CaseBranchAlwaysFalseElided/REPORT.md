# CaseBranchAlwaysFalseElided

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 28  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests entirely on CASE's first-true-branch selection semantics, and RuleScript's core language has no conditional/IF-THEN-ELSE construct, so the before-side CASE can only be expressed as a name-keyed uninterpreted predicate. QED by design treats uninterpreted predicate symbols as mutually independent (the "predicate inference/entailment between independent symbols" limitation), so no SMT proof can relate that opaque symbol to the AND/OR/NOT expansion built from other uninterpreted symbols — and this holds even under the rule's own non-nullable-condition guard that removes the 3VL divergence, since the CASE symbol's meaning simply isn't in the prover. Consequently no narrower special case is provable either: any before side containing the CASE symbol is equally invisible to QED, matching the AGREE-verified SKIPPED precedents for structurally identical CASE-branch rewrites.
