# FoldFloorDivOne

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

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

The rule's correctness rests on the numeric identity "floor division by 1 is the identity function" (plus a value-preserving cast), but RuleScript can only express `floor_div` and `cast` as uninterpreted generic projection operators, and QED's SMT encoding has no integer-arithmetic axioms connecting those fresh uninterpreted function symbols to the literal 1 — so `floor_div(x, 1) = x` (or `= cast(x)`) is false in some interpretations and any faithful encoding (projection, predicate, or join-based) is refuted by a countermodel. A genuine `try_rule` attempt (e.g. `scan.project(floor_div(col, 1))` vs `scan.project(col)`) would return not-provable for the same reason the porter cited, and the gap is not closable by `extend_dsl_file` (the DSL also lacks an integer-literal builder, but that is secondary — the fundamental wall is QED treating scalar arithmetic operators as uninterpreted functions). ```
