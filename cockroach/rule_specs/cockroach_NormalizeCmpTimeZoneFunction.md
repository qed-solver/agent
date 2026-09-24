# Name: NormalizeCmpTimeZoneFunction
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpTimeZoneFunction normalizes timezone functions within
comparison operators. It only matches expressions when:

1. The left side of the comparison is a timezone() function.
2. The second argument to timezone() is a variable of type TIMESTAMP.
3. The right side of the comparison is a constant value TIMESTAMPTZ.

Here's an example:

timezone('America/Denver', ts) = '2020-06-01 12:35:55-07'
=>
ts = timezone('America/Denver', '2020-06-01 12:35:55-07')

This normalization is valid because the overloaded function timezone(zone,
TIMESTAMP) is the inverse of timezone(zone, TIMESTAMPTZ).

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpTimeZoneFunction`, not the other rules in that file):

```
# NormalizeCmpTimeZoneFunction normalizes timezone functions within
# comparison operators. It only matches expressions when:
#
#   1. The left side of the comparison is a timezone() function.
#   2. The second argument to timezone() is a variable of type TIMESTAMP.
#   3. The right side of the comparison is a constant value TIMESTAMPTZ.
#
# Here's an example:
#
#   timezone('America/Denver', ts) = '2020-06-01 12:35:55-07'
#   =>
#   ts = timezone('America/Denver', '2020-06-01 12:35:55-07')
#
# This normalization is valid because the overloaded function timezone(zone,
# TIMESTAMP) is the inverse of timezone(zone, TIMESTAMPTZ).
[NormalizeCmpTimeZoneFunction, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Function $args:* $private:(FunctionPrivate "timezone"))
    $right:(ConstValue) &
        (IsTimestampTZ $right) &
        (Let ($zone $ts $ok):(ScalarPair $args) $ok) &
        (IsTimestamp $ts)
)
=>
((OpName) $ts (MakeTimeZoneFunction $zone $right))
```
