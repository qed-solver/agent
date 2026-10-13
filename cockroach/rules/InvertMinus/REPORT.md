# InvertMinus

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

InvertMinus rewrites -(a - b) to (b - a) if the operand types allow it.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `InvertMinus`, not the other rules in that file):

```
# InvertMinus rewrites -(a - b) to (b - a) if the operand types allow it.
[InvertMinus, Normalize]
(UnaryMinus
    (Minus $left:* $right:*) &
        (CanConstructBinary Minus $right $left)
)
=>
(Minus $right $left)
```
```

## Independent verifier review

**Verdict:** AGREE

In RuleScript, scalar operators like `Minus` and `UnaryMinus` are expressed as uninterpreted projection symbols (`RexRN.Proj` wrapping a `SqlOperator`), and QED's SMT theory (equality + uninterpreted functions + bag counts) carries no arithmetic axioms connecting distinct function symbols, so the identity `UnaryMinus(Minus(a,b)) = Minus(b,a)` is not a theorem — a countermodel exists where the two uninterpreted functions are independently defined. No DSL extension can close this gap because the missing knowledge lives in the prover's theory (the JSON format only supports table-level "guaranteed" constraints, not function-level algebraic axioms), and the prover is the unmodifiable trusted arbiter. ```
