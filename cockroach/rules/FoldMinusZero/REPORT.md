# FoldMinusZero

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

FoldMinusZero folds $left - 0 for numeric types. This rule requires a check
that $left is numeric because JSON - INT is valid and is not a no-op with a
zero value.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldMinusZero`, not the other rules in that file):

```
# FoldMinusZero folds $left - 0 for numeric types. This rule requires a check
# that $left is numeric because JSON - INT is valid and is not a no-op with a
# zero value.
[FoldMinusZero, Normalize]
(Minus $left:(IsAdditiveType (TypeOf $left)) $right:(Const 0))
=>
(Cast $left (BinaryType Minus $left $right))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's core is the numeric identity `x - 0 = cast(x)`, which needs QED to (a) name the numeric constant 0 — the DSL only exposes boolean literals (`RexRN.trueLiteral`/`falseLiteral`), and a `Scan` is an arbitrary relation, so there is no "single row containing 0" to join against — and (b) have arithmetic axioms connecting `minus` and `cast`; QED models non-boolean scalar operators as uninterpreted functions over uninterpreted types with no axioms relating distinct operator symbols, so for any faithful encoding the SMT solver can assign different functions to `minus` and `cast` and refute the equality. This is a fundamental limitation of the prover's theory (the "Peano arithmetic" in qed.pdf is used only for bag-multiplicity counting, not data-value arithmetic), and extending the DSL cannot close it since the axiom gap lives in the prover — consistent with the independently reviewed FoldDivOne precedent from the same file, which has the identical structure (`x / 1 = cast(x)`). ```
