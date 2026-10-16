# SimplifyAnyScalarArray

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 16  **Verification rounds used:** 5

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

The rule is a scalar representation normalization whose soundness rests on a backend-specific collection semantic — that a constant array and the element-wise constant tuple built from it denote interchangeable collections for an element-wise ANY comparison — and QED's Q-expression format has no array/tuple *value* type (only scalar literals, column refs, and uninterpreted calls) and no axiom linking a const array to its tuple conversion. The DSL's `Any`/`ArrayConst`/`TupleConst` all desugar to the identical Or-of-element-comparisons, so before and after collapse to the same Q-expression (a vacuous P=P that checks nothing about the transformation); keeping the two constants as distinct terms instead yields two unrelated uninterpreted constants the SMT solver cannot equate. No `extend_dsl_file` can close this because the Rust prover's Q format is fixed and cannot be given array/tuple value semantics, so the rule is genuinely outside what RuleScript+QED can prove non-vacuously. ```
