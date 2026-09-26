# FoldGroupByAndWindow

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 61  **Verification rounds used:** 4
**Scope detail:** one window function whose output is a partition-determined aggregate, and every GroupBy aggregate references only Window-input cols (case 5a); the 5b ConstAgg/FirstAgg pass-through of window outputs needs per-aggregate algebra QED lacks.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core logic of FoldGroupByAndWindow for case 5a: it models the Window as a per-partition aggregate joined back to the input (conditions 2–4), groups by the partition column (condition 3), and shows that a GroupBy aggregate referencing only window-input columns (case 5a) can be pushed below the eliminated window. The before/after are structurally distinct (Join present vs. absent), the symbol sharing is correct (same k/v types, distinct agg names "w"/"a"), the join fields and INNER kind are right, and the PARTIAL scope tag honestly documents the exclusion of case 5b (ConstAgg pass-through requiring per-aggregate algebra QED lacks).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 21607875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 47222250
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 948458
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1419666
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 45634959
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 47559417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 110046458
  }
}
```
