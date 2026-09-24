# Name: SimplifyAnyScalarArray
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

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
