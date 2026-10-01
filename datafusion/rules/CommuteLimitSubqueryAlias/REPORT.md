# CommuteLimitSubqueryAlias

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_limit.rs, lines 193-199
```

## Independent verifier review

**Verdict:** AGREE

SubqueryAlias is a pure name change — bag-identity — and RuleScript's core language has no operator for it (faithfully omitting it is the only bag-level model), while per qed.pdf §6.2 QED has no model for Limit's ordering/top-N semantics (Sort/Limit/Offset are uninterpreted with no algebraic laws), so before and after both reduce to the identical uninterpreted Limit over the same input. The rule's only semantic content — that alias transparency preserves Limit's row-position semantics — is exactly what QED's bag model cannot express, and even adding a Limit/Sort builder via DSL extension would not help, since the prover itself has no ordering semantics to reason over, leaving any encodable version a vacuous self-equality rather than a real proof of the rule. ```
