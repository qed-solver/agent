# EliminateGroupBy

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 46  **Verification rounds used:** 3

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

QED encodes group-by aggregate calls as uninterpreted operators whose only equational power is bag-equality of their inputs, and it has no axiom that AnyNotNullAgg/ConstAgg over a strict-key (singleton) group collapses to the identity on its element — which is exactly the identity this rule needs, since the after() side removes the aggregate entirely, leaving a bare column that no bag-equality argument can equate to an opaque F(bag) (the EliminateAggDistinctForKeys precedent confirms QED's aggregate reasoning stops at bag-vs-set input equality). So every instance with at least one aggregate call is unprovable for a fundamental prover limitation, not a missing DSL builder that extend_dsl_file could supply (the JSON format only carries operator names, and the prover is the unmodifiable arbiter), while the only provable degenerate instance — zero aggregates over a unique-key scan — reduces to exactly the already-proven cockroach EliminateDistinct (strict-key GroupBy ⇒ Project) shape. ```
