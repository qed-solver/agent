# NormalizeCmpTimeZoneFunction

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness depends on the inverse relationship between CockroachDB's two timezone overloads (timezone(zone, TIMESTAMP) ↔ timezone(zone, TIMESTAMPTZ)), which is a bespoke algebraic property of a backend-specific operator; QED models all non-built-in operators as uninterpreted functions with no axiom mechanism, and no encoding (shared or independent symbols) can make cmp(f(z,x), c) ⟺ cmp(x, g(z,c)) provable without asserting f∘g = id, which the DSL cannot express and the Rust prover cannot be modified to accept. ```
