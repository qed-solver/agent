# HoistValuesSubquery

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 101  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistValuesSubquery extracts subqueries from row tuples and joins them with
the Values operator. This and other subquery hoisting patterns create a
single, top-level relational query with no nesting.

This rule is marked as low priority for the same reason as HoistSelectExists.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistValuesSubquery`, not the other rules in that file):

```
# HoistValuesSubquery extracts subqueries from row tuples and joins them with
# the Values operator. This and other subquery hoisting patterns create a
# single, top-level relational query with no nesting.
#
# This rule is marked as low priority for the same reason as HoistSelectExists.
[HoistValuesSubquery, Normalize, LowPriority]
(Values
    $rows:[ ... $item:* & (HasHoistableSubquery $item) ... ]
    $private:*
)
=>
(HoistValuesSubquery $rows $private)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness hinges on the scalar-subquery cardinality invariant — that the subquery relation R has at most one row — which makes `Values(row_containing_subquery(R))` bag-equivalent to a cross-join with R. QED reasons over arbitrary uninterpreted bags and cannot verify this ≤1-row constraint for a symbol R; any attempt to encode the before/after pair would be refuted by a two-row instantiation of R. Additionally, the DSL has no constructor for a Values node carrying non-trivial row content or a RexSubQuery expression in a value-producing (non-boolean) position, and even extending the DSL with such constructors would not close the gap, since QED's semiring translation has no modeled semantics for a subquery populating a row value — only boolean-context effects (EXISTS/IN) are recognized. This is a genuine fundamental limitation of QED's relational-algebra fragment, not a missed encoding. ```
