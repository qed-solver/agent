# CaseNoBranchesTrueToElse

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 9  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
```

## Independent verifier review

**Verdict:** AGREE

The rule's core claims (CASE WHEN true THEN A ELSE B → A, and dropping false branches) require an interpreted value-level ite/CASE operator, but RuleScript's surface language (RexRN) only exposes And/Or/Not/bool-literals as interpreted scalar ops and emits any CASE as a named RexCall that the fixed Rust prover treats as an uninterpreted function it quantifies universally over — so case(true,A,B)=A is not entailed for arbitrary instantiations. No builder can be added via extend_dsl_file to make the prover interpret CASE (the prover is the unchangeable arbiter and only special-cases the boolean connectives, with ite being internal to their 3-valued-logic encoding, not a query-fragment operator), and a relational re-encoding (union of filtered projections) would bake the CASE semantics in by hand, proving a tautology rather than the rule. This is a genuine QED limitation of the "uninterpreted operator whose internal semantics the prover cannot see through" class, consistent with the two prior AGREE verdicts.
