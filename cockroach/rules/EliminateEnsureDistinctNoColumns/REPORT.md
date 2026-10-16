# EliminateEnsureDistinctNoColumns

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 102  **Verification rounds used:** 6

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateEnsureDistinctNoColumns is similar to EliminateDistinctNoColumns,
except that Max1Row will raise an error if there are no grouping columns and
the input has more than one row. No grouping columns means there is at most
one group. And the Max1Row operator is needed to raise an error if that group
has more than one row, which is a requirement of the EnsureDistinct and
EnsureUpsertDistinct operators.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateEnsureDistinctNoColumns`, not the other rules in that file):

```
# EliminateEnsureDistinctNoColumns is similar to EliminateDistinctNoColumns,
# except that Max1Row will raise an error if there are no grouping columns and
# the input has more than one row. No grouping columns means there is at most
# one group. And the Max1Row operator is needed to raise an error if that group
# has more than one row, which is a requirement of the EnsureDistinct and
# EnsureUpsertDistinct operators.
[EliminateEnsureDistinctNoColumns, Normalize]
(EnsureDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* & (HasNoGroupingCols $groupingPrivate)
)
=>
(ConstructProjectionFromDistinctOn
    (Max1Row $input (ErrorOnDup $groupingPrivate))
    (MakeEmptyColSet)
    $aggregations
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's essential mechanism is error-raising — EnsureDistinctOn/Max1Row/ErrorOnDup must fail when the no-grouping-columns input has more than one row — and QED only models total bag equivalence with no error/partial-function semantics and no Max1Row/Sort/Limit (top-1 ordering) operator to encode it. In the error-free remainder (≤1-row input), the single group's bag coincides with Max1Row's output, so both sides reduce to the identical bag transformation (aggregates over the input, 0 rows on empty input), yielding only a trivial identity rather than a two-sided rule. Additionally, QED's ungrouped Aggregate always emits one row, so it cannot match the required 0/1-row collapse on empty input, and no combination of Filter/Join/Union/Empty can synthesize the missing error or emptiness-conditional behavior.
