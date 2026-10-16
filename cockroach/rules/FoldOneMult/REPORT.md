# FoldOneMult

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

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

FoldOneMult is a purely scalar normalization whose only semantic content is the identity 1·x = cast_τ(x) (multiply-by-one is the value identity, wrapped in a type-correcting cast), but QED's fixed theory provides no multiplication or cast axioms — only equality, uninterpreted functions, order, nat-addition, and ite — so any faithful encoding, in which `Mult` and `Cast` can enter only as generic uninterpreted operator symbols (and the literal `1` is not even expressible, since `RexRN.Const` hardcodes 0), is refuted by an SMT countermodel for every instantiation. This is a genuine prover-theory limitation, not a missing DSL builder: `extend_dsl_file` can add operators but cannot add axioms, because the axioms live in the trusted, unchangeable Rust prover, and no non-vacuous special case avoids the multiply-by-one identity that *is* the rule. Hence the UNSUPPORTED call stands, consistently with the independently verified AGREE/SKIPPED verdicts for the mirror-image siblings FoldMultOne and FoldMinusZero.
