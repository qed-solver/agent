# Name: FoldGroupingOperators
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

FoldGroupingOperators folds two grouping operators into one equivalent
operator. As an example, the following pairs of queries are equivalent:

SELECT sum(t) FROM (SELECT sum(b) FROM ab GROUP BY a) AS g(t);
=>
SELECT sum(b) FROM ab;

SELECT max(t) FROM (SELECT max(b) FROM ab GROUP BY a) AS g(t);
=>
SELECT max(b) FROM ab;

SELECT sum_int(t) FROM (SELECT count(b) FROM ab GROUP BY a) AS g(t);
=>
SELECT count(b) FROM ab;

SELECT DISTINCT ON (x), x, y
FROM (SELECT DISTINCT ON (a, b) a, b FROM ab) AS f(x y)
=>
SELECT DISTINCT ON (a) a, b FROM ab;

This transformation is possible when the following conditions are met:

1. All of the outer aggregates either aggregate on:
A. the output columns of the inner aggregates
B. a grouping column of the inner grouping operator.
2. All of the inner-outer aggregate pairs can be replaced with an equivalent
single aggregate. (See the AggregatesCanMerge comment in operator.go).
3. All of the outer aggregates that aggregate on inner grouping columns ignore
duplicate values (See AggregateIgnoresDuplicates comment in operator.go).
4. The grouping columns of the inner operator functionally determine the
grouping columns of the outer operator according to the functional
dependencies of the input of the inner operator.
5. Both grouping operators are unordered.

Why is it sufficient for the inner grouping columns to functionally determine
the outer grouping columns?
* Duplicate values in the determinant ("from" side) imply duplicate values in
the dependent ("to" side).
* Grouping on the determinant will not remove unique values from the
determinant. Therefore, the grouping will not remove unique values from the
dependent, by the properties of functional dependencies.
* Grouping on the dependent will simply reduce the dependent to its unique
values.
* Therefore, grouping on the dependent produces the same final groups as
grouping on the dependent after grouping on the determinant.
* The conditions guarantee that the aggregates produce the same result
regardless of how the grouping is accomplished, as long as the same groups
result in the end.

Take the following table as an example:

r a b
-----
1 4 3
2 4 3
3 2 3
4 2 3
5 6 5
6 6 5

Its functional dependencies: key(r), r-->(a, b), a-->(b)

Here are some examples of possible groupings taking the sum over the "r"
column:

Grouping by a: SUM(1, 2), SUM(3, 4), SUM(5, 6)
Grouping by b: SUM(1, 2, 3, 4), SUM(5, 6)
Grouping by a then b: SUM(SUM(1, 2), SUM(3, 4)), SUM(SUM(5, 6))

Rows can always be grouped together by subsequent groupings, but they can
never be "ungrouped". Grouping on a does not group any rows together that
would not also be grouped by b.

This situation is rare in direct SQL queries, but can arise when composing
views and queries.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `FoldGroupingOperators`, not the other rules in that file):

```
# FoldGroupingOperators folds two grouping operators into one equivalent
# operator. As an example, the following pairs of queries are equivalent:
#
#   SELECT sum(t) FROM (SELECT sum(b) FROM ab GROUP BY a) AS g(t);
#   =>
#   SELECT sum(b) FROM ab;
#
#   SELECT max(t) FROM (SELECT max(b) FROM ab GROUP BY a) AS g(t);
#   =>
#   SELECT max(b) FROM ab;
#
#   SELECT sum_int(t) FROM (SELECT count(b) FROM ab GROUP BY a) AS g(t);
#   =>
#   SELECT count(b) FROM ab;
#
#   SELECT DISTINCT ON (x), x, y
#   FROM (SELECT DISTINCT ON (a, b) a, b FROM ab) AS f(x y)
#   =>
#   SELECT DISTINCT ON (a) a, b FROM ab;
#
# This transformation is possible when the following conditions are met:
#
# 1. All of the outer aggregates either aggregate on:
#      A. the output columns of the inner aggregates
#      B. a grouping column of the inner grouping operator.
# 2. All of the inner-outer aggregate pairs can be replaced with an equivalent
#    single aggregate. (See the AggregatesCanMerge comment in operator.go).
# 3. All of the outer aggregates that aggregate on inner grouping columns ignore
#    duplicate values (See AggregateIgnoresDuplicates comment in operator.go).
# 4. The grouping columns of the inner operator functionally determine the
#    grouping columns of the outer operator according to the functional
#    dependencies of the input of the inner operator.
# 5. Both grouping operators are unordered.
#
# Why is it sufficient for the inner grouping columns to functionally determine
# the outer grouping columns?
# * Duplicate values in the determinant ("from" side) imply duplicate values in
#   the dependent ("to" side).
# * Grouping on the determinant will not remove unique values from the
#   determinant. Therefore, the grouping will not remove unique values from the
#   dependent, by the properties of functional dependencies.
# * Grouping on the dependent will simply reduce the dependent to its unique
#   values.
# * Therefore, grouping on the dependent produces the same final groups as
#   grouping on the dependent after grouping on the determinant.
# * The conditions guarantee that the aggregates produce the same result
#   regardless of how the grouping is accomplished, as long as the same groups
#   result in the end.
#
# Take the following table as an example:
#
#   r a b
#   -----
#   1 4 3
#   2 4 3
#   3 2 3
#   4 2 3
#   5 6 5
#   6 6 5
#
# Its functional dependencies: key(r), r-->(a, b), a-->(b)
#
# Here are some examples of possible groupings taking the sum over the "r"
# column:
#
# Grouping by a: SUM(1, 2), SUM(3, 4), SUM(5, 6)
# Grouping by b: SUM(1, 2, 3, 4), SUM(5, 6)
# Grouping by a then b: SUM(SUM(1, 2), SUM(3, 4)), SUM(SUM(5, 6))
#
# Rows can always be grouped together by subsequent groupings, but they can
# never be "ungrouped". Grouping on a does not group any rows together that
# would not also be grouped by b.
#
# This situation is rare in direct SQL queries, but can arise when composing
# views and queries.
[FoldGroupingOperators, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    (GroupBy | DistinctOn
        $innerInput:*
        $innerAggs:*
        $innerGrouping:* & (IsUnorderedGrouping $innerGrouping)
    )
    $outerAggs:*
    $outerGrouping:* &
        (IsUnorderedGrouping $outerGrouping) &
        (ColsAreDeterminedBy
            $outerGroupingCols:(GroupingCols $outerGrouping)
            $innerGroupingCols:(GroupingCols $innerGrouping)
            $innerInput
        ) &
        (CanMergeAggs $innerAggs $outerAggs $innerGroupingCols)
)
=>
((OpName)
    $innerInput
    (MergeAggs $innerAggs $outerAggs $innerGroupingCols)
    (MakeGrouping $outerGroupingCols (EmptyOrdering))
)
```
