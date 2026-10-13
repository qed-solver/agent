# PushAggDistinctIntoGroupBy

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 31  **Verification rounds used:** 3
**Scope detail:** the GroupBy/ScalarGroupBy has exactly one aggregation, a single-argument DISTINCT call, with two grouping columns and no non-distinct aggregations, whereas the source rule matches any single aggregate function with any input arguments over any grouping set.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

PushAggDistinctIntoGroupBy pushes an aggregate function DISTINCT modifier into
the input of a GroupBy or ScalarGroupBy operator. This allows the optimizer to
take advantage of an index on the column(s) subject to the DISTINCT operation.
PushAggDistinctIntoGroupBy can match any single aggregate function, including
those that have multiple input arguments.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `PushAggDistinctIntoGroupBy`, not the other rules in that file):

```
# PushAggDistinctIntoGroupBy pushes an aggregate function DISTINCT modifier into
# the input of a GroupBy or ScalarGroupBy operator. This allows the optimizer to
# take advantage of an index on the column(s) subject to the DISTINCT operation.
# PushAggDistinctIntoGroupBy can match any single aggregate function, including
# those that have multiple input arguments.
[PushAggDistinctIntoGroupBy, Normalize]
(GroupBy | ScalarGroupBy
    $input:*
    $aggregations:[
        $item:(AggregationsItem (AggDistinct $agg:*) $aggColID:*)
    ]
    $groupingPrivate:*
)
=>
((OpName)
    (DistinctOn
        $input
        (MakeAggCols
            FirstAgg
            (OrderingCols
                (ExtractGroupingOrdering $groupingPrivate)
            )
        )
        (MakeGrouping
            (UnionCols
                (GroupingCols $groupingPrivate)
                (ExtractAggInputColumns $agg)
            )
            (EmptyOrdering)
        )
    )
    [ (AggregationsItem $agg $aggColID) ]
    $groupingPrivate
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-vacuous instance of the source rule: before() is a GroupBy whose aggregation list is exactly one DISTINCT aggregate (matching the source rule's own single-item `$aggregations` match condition, which the `...`-free pattern in Optgen syntax enforces), and after() is the rule's rewrite — a no-aggregate group-by acting as DistinctOn over grouping cols ∪ distinct-arg cols, feeding the same `f` without the distinct flag over the same grouping keys. Symbol sharing is exactly right (f, k1, k2, x shared across both sides as the rule requires, with no accidental over-constraint), and there are no preconditions to miss because the source rule carries no side-conditions; the proof can only go through if the prover folds the DISTINCT flag into input deduplication and the inner group-by into key deduplication, which is precisely the bag-structural content of the rewrite. The PARTIAL scope line is honest and specific — the real restrictions (single-argument distinct call, two non-empty grouping columns, and no grouping ordering, which the DSL doesn't model at all) are stated, while "exactly one aggregation / no non-distinct aggregations" is the source rule's own match requirement rather than a hidden assumption — so the narrowing is a useful, non-degenerate special case. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 13112920
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 47064584
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 954083
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 910000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30120375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 47321000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 93598292
  }
}
```
