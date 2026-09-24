# EliminateAggDistinctForKeys

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateAggDistinctForKeys eliminates unnecessary AggDistinct modifiers when
it is known that the aggregation argument is unique within each group.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateAggDistinctForKeys`, not the other rules in that file):

```
# EliminateAggDistinctForKeys eliminates unnecessary AggDistinct modifiers when
# it is known that the aggregation argument is unique within each group.
[EliminateAggDistinctForKeys, Normalize]
(GroupBy | ScalarGroupBy
    $input:* & (HasStrictKey $input)
    $aggregations:[
        ...
        $item:(AggregationsItem (AggDistinct $agg:*))
        ...
    ]
    $groupingPrivate:* &
        (CanRemoveAggDistinctForKeys
            $input
            $groupingPrivate
            $agg
        )
)
=>
((OpName)
    $input
    (ReplaceAggregationsItem $aggregations $item $agg)
    $groupingPrivate
)
```
```

## Independent verifier review

**Verdict:** AGREE

`EliminateAggDistinctForKeys` rewrites `AGG(DISTINCT x)` to `AGG(x)` on the premise that, when the grouping columns plus x form a strict key, x has no duplicate values within any group — i.e. its correctness rests on the aggregate identity "DISTINCT is a no-op when the argument is unique per group." QED models each aggregate invocation (the DISTINCT and non-DISTINCT forms being distinct function symbols) as an uninterpreted function and knows no algebra connecting them; its only aggregate reasoning is bag-equality of the input for the *same* function, which cannot apply because the rewrite changes the function itself. Hence no encoding — including the narrow "x is the table's primary key" special case, where the key condition is even expressible via `scan(unique=true)` — can reduce the two sides to the same aggregate over equal input bags, so the rule is genuinely outside QED's power (a missing aggregate-algebra capability in the prover, not a DSL gap that `extend_dsl_file` could close). ```
