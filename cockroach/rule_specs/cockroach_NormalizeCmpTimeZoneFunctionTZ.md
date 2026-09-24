# Name: NormalizeCmpTimeZoneFunctionTZ
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpTimeZoneFunctionTZ normalizes timezone functions within
comparison operators. It only matches expressions when:

1. The left side of the comparison is a timezone() function.
2. The second argument to timezone() is a variable of type TIMESTAMPTZ.
3. The right side of the comparison is a constant value TIMESTAMP.

Here's an example:

timezone('America/Denver', tz) = '2020-06-01 12:35:55'
=>
tz = timezone('America/Denver', '2020-06-01 12:35:55')

This normalization is possible because the overloaded function timezone(zone,
TIMESTAMPTZ) is the inverse of timezone(zone, TIMESTAMP).

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpTimeZoneFunctionTZ`, not the other rules in that file):

```
# NormalizeCmpTimeZoneFunctionTZ normalizes timezone functions within
# comparison operators. It only matches expressions when:
#
#   1. The left side of the comparison is a timezone() function.
#   2. The second argument to timezone() is a variable of type TIMESTAMPTZ.
#   3. The right side of the comparison is a constant value TIMESTAMP.
#
# Here's an example:
#
#   timezone('America/Denver', tz) = '2020-06-01 12:35:55'
#   =>
#   tz = timezone('America/Denver', '2020-06-01 12:35:55')
#
# This normalization is possible because the overloaded function timezone(zone,
# TIMESTAMPTZ) is the inverse of timezone(zone, TIMESTAMP).
[NormalizeCmpTimeZoneFunctionTZ, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Function $args:* $private:(FunctionPrivate "timezone"))
    $right:(ConstValue) &
        (IsTimestamp $right) &
        (Let ($zone $tz $ok):(ScalarPair $args) $ok) &
        (IsTimestampTZ $tz) &
        ^(IsConstValueOrGroupOfConstValues $tz)
)
=>
((OpName) $tz (MakeTimeZoneFunction $zone $right))
```
