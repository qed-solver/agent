# FoldOneMult

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

FoldOneMult folds 1 * $right for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldOneMult`, not the other rules in that file):

```
# FoldOneMult folds 1 * $right for numeric types.
[FoldOneMult, Normalize]
(Mult $left:(Const 1) $right:*)
=>
(Cast $right (BinaryType Mult $left $right))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on scalar arithmetic axioms — the right-identity law 1·x = x and value-preservation of the cast — but QED's SMT encoding lowers Mult and Cast to uninterpreted functions (with no arithmetic axioms anywhere in the prover; the only interpreted scalar logic is boolean connectives/equality), so the required statement ∀x. Mult(1, x) = Cast(x) has a trivial countermodel and no DSL extension can help, since the trusted Rust prover's encoding cannot be changed and adding a numeric literal to the DSL would still leave both operators uninterpreted. The only provable "encoding" would be a vacuous one (reusing a single symbol on both sides, or reinterpreting the rule as a boolean AND-with-TRUE identity), neither of which is a faithful port of the numeric rule — the same fundamental limitation that justifies the SKIPPED status of the structurally identical siblings FoldMultOne and FoldDivOne. ```
