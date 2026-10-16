# NormalizeCmpTimeZoneFunction

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 45  **Verification rounds used:** 3

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

The rule is a purely scalar rewrite whose validity depends on the two timezone overloads being mutual (order-preserving) inverses, but QED treats timezone and the comparison operators as uninterpreted symbols with no inverse/monotonicity axioms, so it correctly finds counter-instances and no non-vacuous encoding of any of Eq/Ge/Gt/Le/Lt is provable. The only constraint channel in the JSON is the per-table `guaranteed` field (row-level predicates over a single table's columns via QedTable.getConstraints()), which cannot express a first-order axiom over scalar function symbols, and RRule exposes no rule-level axiom API. Since the trusted Rust prover is unmodifiable, extending the DSL alone cannot supply the missing function axioms, so the limitation is fundamental, not a porter error. ```
