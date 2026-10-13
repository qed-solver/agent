# EliminateGroupBy

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 30  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateGroupBy is similar to EliminateDistinct, but it operates on GroupBy
expressions where the grouping columns are statically known to form a strict
key in the GroupBy's input.

It only applies if all the aggregate functions are ConstAgg, ConstNotNullAgg,
or AnyNotNullAgg. These aggregate functions all evaluate to the first non-NULL
value encountered, and NULL if there are no such values. Because the input is
guaranteed to produce one row per group, these aggregate functions are
equivalent to projecting their input column.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateGroupBy`, not the other rules in that file):

```
# EliminateGroupBy is similar to EliminateDistinct, but it operates on GroupBy
# expressions where the grouping columns are statically known to form a strict
# key in the GroupBy's input.
#
# It only applies if all the aggregate functions are ConstAgg, ConstNotNullAgg,
# or AnyNotNullAgg. These aggregate functions all evaluate to the first non-NULL
# value encountered, and NULL if there are no such values. Because the input is
# guaranteed to produce one row per group, these aggregate functions are
# equivalent to projecting their input column.
[EliminateGroupBy, Normalize]
(GroupBy
    $input:*
    $aggs:* & (AreAllAnyNotNullAggs $aggs)
    $groupingPrivate:* &
        (ColsAreStrictKey (GroupingCols $groupingPrivate) $input)
)
=>
(Project
    $input
    (ConvertAnyNotNullAggsToProjections $aggs)
    (IntersectionCols
        (GroupingOutputCols $groupingPrivate $aggs)
        (OutputCols $input)
    )
)
```
```

## Independent verifier review

**Verdict:** AGREE

EliminateGroupBy's entire soundness rests on the algebraic identity "AggAnyNotNull/ConstNotNull over a ≤1-row group returns that row's value (or NULL)", i.e. relating an aggregate output column to a plain field — in QED, aggregate calls are serialized as uninterpreted operator names (via `genericAggregateOp` in `Aggregate.semantics()`) and are only equated when they share a name over provably-equal group bags, so the prover has no axiom connecting `f({(k,x)})` to `x`, exactly the aggregate-algebra limitation QED's own evaluation documents ("knows nothing about a specific aggregate function's algebra beyond bag equality of its input"). This is a limitation of the unmodifiable Rust prover, not a missing DSL builder: the JSON format only carries an aggregate's name/operands, so `extend_dsl_file` cannot add a "sole-value" aggregate or its axiom, and no alternative encoding can remove the uninterpreted aggregate from the before side, which the rule defines. The porter's most-faithful instance (single-column unique scan, key = grouping key = aggregated value) is therefore the maximal encodable case, and its genuine `provable: false` verdict confirms the rule is outside QED's reach.
