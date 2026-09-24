# Name: SimplifyEqualsAnyTuple
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

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
