# NormalizeCmpTimeZoneFunctionTZ

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 62  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on an axiom QED does not and cannot have — that the two `timezone` overloads are mutual inverses (and order-preserving for Ge/Gt/Le/Lt) — but in RuleScript they can only be modeled as independent uninterpreted function symbols, and `f(z, tz) OP c ⟺ tz OP g(z, c)` is not valid for arbitrary f, g under any instantiation. No encoding closes the gap: per-table `guaranteed` constraints are row-scoped over a table's own columns and cannot assert a functional-inverse law at the query's arbitrary non-row constant, the prover's JSON format has no axiom channel, and `extend_dsl_file` only extends the Java DSL, not the unmodifiable Rust prover. My own fresh exploration of alternative encodings (relational joins over a representation-relation table, threading the constant through a scan) either still requires relating two independent uninterpreted symbols or degenerates into a trivial identity that no longer states the rule — so this is the documented limitation of no predicate entailment between independent symbols / no access to a backend operator's bespoke semantics, matching the independently-verified skip of the type-swap sibling.
