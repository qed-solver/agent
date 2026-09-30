# SimplifyAnyScalarArray

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyAnyScalarArray converts a scalar ANY operation on a constant ARRAY to a scalar
ANY operation on a tuple. In particular, this allows SimplifyEqualsAnyTuple to be
triggered, which allows constraints to be generated.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyAnyScalarArray`, not the other rules in that file):

```
# SimplifyAnyScalarArray converts a scalar ANY operation on a constant ARRAY to a scalar
# ANY operation on a tuple. In particular, this allows SimplifyEqualsAnyTuple to be
# triggered, which allows constraints to be generated.
[SimplifyAnyScalarArray, Normalize]
(AnyScalar $input:* $ary:(Const) & (IsConstArray $ary) $cmp:*)
=>
(AnyScalar $input (ConvertConstArrayToTuple $ary) $cmp)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's only non-trivial content is that a constant array and a tuple packaging the same elements denote the same operands for ANY — a definitional identity between two CockroachDB value constructors, which QED's Q-expression language cannot express: it has no array/tuple value constructors, no list semantics, and no axioms relating container terms. Consequently every encoding collapses to a trichotomy — expand both sides to the element-wise OR / semi-join over a shared symbol (structurally identical, vacuous), or keep the array and tuple as distinct uninterpreted symbols (SMT countermodel, unprovable) — and no narrower special case escapes this, since even the one-element instance is definitionally an identity; the missing container-value semantics lives in the fixed prover's value language, not in a shape a `RelRN`/`RexRN` extension could add.
