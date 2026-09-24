# Name: PushSelectIntoUnorderedDistinctOn
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoUnorderedDistinctOn pushes Select filters into the input of an
unordered DistinctOn. Unlike PushSelectIntoGroupBy, which only pushes filters
on grouping or ConstAgg columns, this rule pushes filters that reference any
column of the DistinctOn's input — including first-agg columns.

This is valid because an unordered DistinctOn can return any row from each
group. Filtering before the DistinctOn simply constrains which rows are
available to be chosen; groups that are entirely eliminated by the filter
produce no output row, which is equivalent to choosing a row and then
filtering it away afterward.

The rule is restricted to unordered DistinctOn: an ordered DistinctOn must
pick a specific row determined by the ordering, so pre-filtering could change
which row is selected.

Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
filters through the EnsureDistinctOn can prevent it from detecting duplicate
rows and therefore change error behavior.

Filters referencing outer columns are not pushed; they remain above the
DistinctOn.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoUnorderedDistinctOn`, not the other rules in that file):

```
# PushSelectIntoUnorderedDistinctOn pushes Select filters into the input of an
# unordered DistinctOn. Unlike PushSelectIntoGroupBy, which only pushes filters
# on grouping or ConstAgg columns, this rule pushes filters that reference any
# column of the DistinctOn's input — including first-agg columns.
#
# This is valid because an unordered DistinctOn can return any row from each
# group. Filtering before the DistinctOn simply constrains which rows are
# available to be chosen; groups that are entirely eliminated by the filter
# produce no output row, which is equivalent to choosing a row and then
# filtering it away afterward.
#
# The rule is restricted to unordered DistinctOn: an ordered DistinctOn must
# pick a specific row determined by the ordering, so pre-filtering could change
# which row is selected.
#
# Note: Do not add EnsureDistinctOn to the match pattern. Pushing the select
# filters through the EnsureDistinctOn can prevent it from detecting duplicate
# rows and therefore change error behavior.
#
# Filters referencing outer columns are not pushed; they remain above the
# DistinctOn.
[PushSelectIntoUnorderedDistinctOn, Normalize]
(Select
    $input:(DistinctOn
        $groupingInput:*
        $aggregations:*
        $groupingPrivate:* &
            (IsUnorderedGrouping $groupingPrivate)
    )
    $filters:[
        ...
        $item:* &
            (IsBoundBy
                $item
                $inputCols:(OutputCols $groupingInput)
            )
        ...
    ]
)
=>
(Select
    (DistinctOn
        (Select
            $groupingInput
            (ExtractBoundConditions $filters $inputCols)
        )
        $aggregations
        $groupingPrivate
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
