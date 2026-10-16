# FoldFloorDivOne

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

FoldFloorDivOne folds $left // 1 for integer types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldFloorDivOne`, not the other rules in that file):

```
# FoldFloorDivOne folds $left // 1 for integer types.
[FoldFloorDivOne, Normalize]
(FloorDiv $left:* $right:(Const 1) & (IsInt $left))
=>
(Cast $left (BinaryType (OpName) $left $right))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on the numeric identity floor_div(x, 1) = x plus the semantics of the int→int cast, but QED's fixed theory (equality, uninterpreted functions, order, addition, ite) contains no division or cast algebra, so `FloorDiv` and `Cast` can only be entered as uninterpreted symbols and no encoding yields a valid relation between floorDiv(x,1) and cast(x,τ) — the only equality-based encoding available (filtering/joining on the terms being equal) is circular, since it assumes exactly the identity the rule claims and QED cannot do entailment between independent uninterpreted symbols. This is compounded by RexRN.Const hardcoding literal 0 (the constant 1 cannot be named at all), and closing it is impossible via extend_dsl_file because the missing axioms live in the QED prover's theory, not the Java DSL — the same fundamental limitation that correctly caused FoldDivOne to be skipped. ```
