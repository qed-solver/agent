# Name: SimplifyLeftJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

SimplifyLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
FullJoin to a RightJoin) when it's known that every row in the join's left
input will match at least one row in the right input. Since every row matches,
NULL-extended rows will never be added by the outer join, and therefore can be
mapped to an InnerJoin (or RightJoin in case of FullJoin). See
filtersMatchAllLeftRows comment for conditions in which this rule can match.

Self-join example:
SELECT * FROM xy LEFT JOIN xy AS xy2 ON xy.y = xy2.y
=>
SELECT * FROM xy INNER JOIN xy AS xy2 ON xy.y = xy2.y

Foreign-key example:
SELECT * FROM orders o LEFT JOIN customers c ON o.customer_id = c.id
=>
SELECT * FROM orders o INNER JOIN customers c ON o.customer_id = c.id

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyLeftJoin`, not the other rules in that file):

```
# SimplifyLeftJoin reduces a LeftJoin operator to an InnerJoin operator (or a
# FullJoin to a RightJoin) when it's known that every row in the join's left
# input will match at least one row in the right input. Since every row matches,
# NULL-extended rows will never be added by the outer join, and therefore can be
# mapped to an InnerJoin (or RightJoin in case of FullJoin). See
# filtersMatchAllLeftRows comment for conditions in which this rule can match.
#
# Self-join example:
#   SELECT * FROM xy LEFT JOIN xy AS xy2 ON xy.y = xy2.y
#   =>
#   SELECT * FROM xy INNER JOIN xy AS xy2 ON xy.y = xy2.y
#
# Foreign-key example:
#   SELECT * FROM orders o LEFT JOIN customers c ON o.customer_id = c.id
#   =>
#   SELECT * FROM orders o INNER JOIN customers c ON o.customer_id = c.id
[SimplifyLeftJoin, Normalize]
(LeftJoin | LeftJoinApply | FullJoin
    $left:*
    $right:*
    $on:* & (JoinFiltersMatchAllLeftRows $left $right $on)
    $private:*
)
=>
(ConstructNonLeftJoin (OpName) $left $right $on $private)
```
