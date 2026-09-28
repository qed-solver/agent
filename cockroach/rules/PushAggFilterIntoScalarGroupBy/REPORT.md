# PushAggFilterIntoScalarGroupBy

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

PushAggFilterIntoScalarGroupBy pushes an aggregate function FILTER
modifier into the input of the ScalarGroupBy operator. This allows the
optimizer to take advantage of an index on the column(s) subject to the
FILTER operation. PushAggFilterIntoScalarGroupBy can match any single
aggregate function, including those that have multiple input arguments.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `PushAggFilterIntoScalarGroupBy`, not the other rules in that file):

```
# PushAggFilterIntoScalarGroupBy pushes an aggregate function FILTER
# modifier into the input of the ScalarGroupBy operator. This allows the
# optimizer to take advantage of an index on the column(s) subject to the
# FILTER operation. PushAggFilterIntoScalarGroupBy can match any single
# aggregate function, including those that have multiple input arguments.
[PushAggFilterIntoScalarGroupBy, Normalize]
(ScalarGroupBy
    $input:*
    $aggregations:[
        $item:(AggregationsItem
            (AggFilter $agg:* $condition:*)
            $aggColID:*
        )
    ]
    $groupingPrivate:*
)
=>
(ScalarGroupBy
    (Select $input [ (FiltersItem $condition) ])
    [ (AggregationsItem $agg $aggColID) ]
    $groupingPrivate
)
```
```

## Independent verifier review

**Verdict:** AGREE

QED models each aggregate call as an uninterpreted function applied to its input bag, and neither the DSL's `AggCall` nor the prover's aggregate JSON object (`{operator, operand, distinct, ignoreNulls, type}`) has any FILTER/row-selection slot — the only modeled pre-aggregate row filter is COUNT's non-null ignoreNulls. So the before side `f(a) FILTER (WHERE c)` has no non-vacuous encoding: expressing the filter as a `Filter` node under the aggregate makes `before` syntactically identical to `after`, while folding `c` into the aggregate's operands produces a different uninterpreted function over a different bag, for which the SMT solver has no algebraic bridge to the after side. This is a fundamental limitation of QED's aggregate semantics (an internal operator behavior it cannot see through), not a missing DSL builder — even a `extend_dsl_file` adding a filter field to the serialized aggregate would be inert, since the immutable prover does not parse or model it, so the porter's UNSUPPORTED conclusion is correct. ```
