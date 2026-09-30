# SimplifyEqualsAnyTuple

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 101  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyEqualsAnyTuple converts a scalar ANY operation to an IN comparison.
It transforms

x = ANY (...)

to

x IN (...)

Which allows scans to be constrained.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyEqualsAnyTuple`, not the other rules in that file):

```
# SimplifyEqualsAnyTuple converts a scalar ANY operation to an IN comparison.
# It transforms
#
#   x = ANY (...)
#
# to
#
#   x IN (...)
#
# Which allows scans to be constrained.
[SimplifyEqualsAnyTuple, Normalize]
(AnyScalar $input:* $tuple:(Tuple) $cmp:* & (OpsAreSame $cmp Eq))
=>
(In $input $tuple)
```
```

## Independent verifier review

**Verdict:** AGREE

This rule is a definitional unfolding of a backend operator: `x = ANY (t1..tn)` being equivalent to the in-list `x = t1 ∨ … ∨ x = tn` holds only by CockroachDB's semantics of `AnyScalar`/`Eq`, and RuleScript's fragment has no way to express either side in interpreted form — there is no interpreted value equality, no scalar constants to stand for the tuple elements, and no ANY/IN-over-a-list operator, so the best faithful encoding is an uninterpreted predicate symbol `P(x, t1..tn)` in `before()` versus an `Or` of other uninterpreted equality symbols in `after()`. QED's SMT-based decision procedure, by design, cannot infer any nontrivial identity between independent uninterpreted symbols (it only reasons about bag algebra and logical connectives over them), and all operator interpretation lives in the fixed Rust prover, so no Java-side DSL/serializer extension can supply the missing semantics. The porter correctly identified a fundamental fragment limitation, not an encoding bug.
