# FoldDivOne

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

FoldDivOne folds $left / 1 for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldDivOne`, not the other rules in that file):

```
# FoldDivOne folds $left / 1 for numeric types.
[FoldDivOne, Normalize]
(Div $left:* $right:(Const 1))
=>
(Cast $left (BinaryType (OpName) $left $right))
```
```

## Independent verifier review

**Verdict:** AGREE

FoldDivOne depends on the numeric constant-folding identity `div(x, 1) = cast(x)`, including a representable literal `1` and the algebraic behavior of division/casting. QED only sees non-boolean scalar operators as uninterpreted function symbols over uninterpreted types, with no numeric literals and no axioms connecting distinct operators, so no faithful encoding can be universally proved.
