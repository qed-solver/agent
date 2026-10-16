# PushAggFilterIntoScalarGroupBy

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 48  **Verification rounds used:** 3

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

The rule's only transformation is hoisting a per-call aggregate FILTER (SELECT agg(x) FILTER(WHERE p) FROM t) into a Filter on the ScalarGroupBy's input, and the DSL's AggCall has no filter modifier (the JSON group node only carries operator/operand/distinct/ignoreNulls), so the before-side can at best be written as the aggregate over the already-filtered input, making both sides identical and proving nothing about FILTER semantics. The genuine equivalence needed is "agg(x) FILTER (WHERE p) over all rows = agg(x) over the rows where p holds", which is exactly the kind of aggregate-modifier internal semantics QED cannot see through: it models aggregates only up to bag equality of their input and knows no aggregate algebra, and the prover cannot be extended to add FILTER semantics. Extending the DSL with a filter field on AggCall would be futile since JSONSerializer has no corresponding field and the Rust QED prover is the immutable arbiter. ```
