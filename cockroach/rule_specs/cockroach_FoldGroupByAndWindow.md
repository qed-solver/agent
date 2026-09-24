# Name: FoldGroupByAndWindow
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

FoldGroupByAndWindow merges a GroupBy operator with an input Window operator.
This is possible when the following conditions are satisfied:

1. The GroupBy is unordered. This may not technically be necessary, but
avoids complication in determining the correctness of ordering-sensitive
aggregations.

2. The window function output cols are functionally determined by the
partition-by cols. This means that the window function outputs the
same value for every row in the partition (group).

3. The Window operator partition-by cols and grouping cols are the same.
This ensures that an aggregate operator will act on the same set of rows,
whether it is part of the Window operator or the GroupBy operator.

4. The window functions are all aggregate functions. This ensures they are
compatible with GroupBy operators.

5. Finally, all of the GroupBy's aggregations must satisfy one of two cases:
a. The aggregate only references cols from the Window operator's input.
b. The aggregate is a ConstAgg (or ConstNotNull, AnyNotNull, or FirstAgg)
that passes through the result of a window function.

Assuming all of the above are satisfied, each GroupBy aggregate that only
references the Window's input can be left alone (5a). Then, each ConstAgg
referencing a window function can be replaced by that function (5b).

Here's an example with slightly altered SQL syntax:

SELECT max(b), const_agg(foo), const_agg(bar)
FROM
(
SELECT *, count(c) OVER w AS foo, array_agg(d) OVER w AS bar
FROM abcd
WINDOW w AS (
PARTITION BY a ORDER BY d
RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
)
)
GROUP BY a;
=>
SELECT max(b), count(c), array_agg(d ORDER BY d) FROM abcd GROUP BY a;

Note also that the Window's ordering should be preserved by the GroupBy to
ensure that ordering-sensitive aggregates produce correct results.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `FoldGroupByAndWindow`, not the other rules in that file):

```
# FoldGroupByAndWindow merges a GroupBy operator with an input Window operator.
# This is possible when the following conditions are satisfied:
#
#   1. The GroupBy is unordered. This may not technically be necessary, but
#      avoids complication in determining the correctness of ordering-sensitive
#      aggregations.
#
#   2. The window function output cols are functionally determined by the
#      partition-by cols. This means that the window function outputs the
#      same value for every row in the partition (group).
#
#   3. The Window operator partition-by cols and grouping cols are the same.
#      This ensures that an aggregate operator will act on the same set of rows,
#      whether it is part of the Window operator or the GroupBy operator.
#
#   4. The window functions are all aggregate functions. This ensures they are
#      compatible with GroupBy operators.
#
#   5. Finally, all of the GroupBy's aggregations must satisfy one of two cases:
#      a. The aggregate only references cols from the Window operator's input.
#      b. The aggregate is a ConstAgg (or ConstNotNull, AnyNotNull, or FirstAgg)
#         that passes through the result of a window function.
#
# Assuming all of the above are satisfied, each GroupBy aggregate that only
# references the Window's input can be left alone (5a). Then, each ConstAgg
# referencing a window function can be replaced by that function (5b).
#
# Here's an example with slightly altered SQL syntax:
#
#   SELECT max(b), const_agg(foo), const_agg(bar)
#   FROM
#     (
#       SELECT *, count(c) OVER w AS foo, array_agg(d) OVER w AS bar
#       FROM abcd
#       WINDOW w AS (
#         PARTITION BY a ORDER BY d
#         RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
#       )
#     )
#   GROUP BY a;
#   =>
#   SELECT max(b), count(c), array_agg(d ORDER BY d) FROM abcd GROUP BY a;
#
# Note also that the Window's ordering should be preserved by the GroupBy to
# ensure that ordering-sensitive aggregates produce correct results.
[FoldGroupByAndWindow, Normalize]
(GroupBy | ScalarGroupBy
    $window:(Window
            $input:*
            $windows:* & (WindowsAreAggregations $windows)
            $windowPrivate:*
        ) &
        (ColsAreDeterminedBy
            (WindowFuncOutputCols $windows)
            $partitionByCols:(WindowPartition $windowPrivate)
            $window
        )
    $aggs:* &
        (CanMergeAggsAndWindow
            $aggs
            $windows
            $inputCols:(OutputCols $input)
        )
    $groupingPrivate:* &
        (IsUnorderedGrouping $groupingPrivate) &
        (ColsAreEqual
            $groupingCols:(GroupingCols $groupingPrivate)
            $partitionByCols
        )
)
=>
((OpName)
    $input
    (MergeAggsAndWindow $aggs $windows $inputCols)
    (MakeGrouping
        (GroupingCols $groupingPrivate)
        (WindowOrdering $windowPrivate)
    )
)
```
