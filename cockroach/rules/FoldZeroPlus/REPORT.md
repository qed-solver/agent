# FoldZeroPlus

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 45  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/numeric.opt

FoldZeroPlus folds 0 + $right for numeric types.

Extracted from `numeric.opt` (which defines multiple rules — implement specifically `FoldZeroPlus`, not the other rules in that file):

```
# FoldZeroPlus folds 0 + $right for numeric types.
[FoldZeroPlus, Normalize]
(Plus $left:(Const 0) $right:*)
=>
(Cast $right (BinaryType Plus $left $right))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on the numeric algebra of its operators — the additive identity law `0 + x = x` (plus the cast to the binary result type) — but in QED's encoding `Plus` and `Cast` are uninterpreted function symbols over lifted data values, and its oracle theory (equality with uninterpreted functions/predicates, with arithmetic only on the internal natural-number multiplicity sort) contains no axioms relating them, so `plus(0, x) = cast(x, T)` is not entailed under arbitrary instantiation. This is not a closable DSL gap: even a zero-literal builder added via `extend_dsl_file` would only introduce another uninterpreted constant, since the missing identity axioms would have to live in the (untouchable) prover. It is exactly the "operator-specific internal semantics QED cannot see through as an uninterpreted function" limitation, consistent with the verified mirror rule FoldPlusZero and its siblings in numeric.opt. ```
