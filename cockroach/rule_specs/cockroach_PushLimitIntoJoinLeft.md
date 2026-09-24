# Name: PushLimitIntoJoinLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoJoinLeft pushes a Limit into the left input of an InnerJoin or
LeftJoin when rows from the left input are guaranteed to be preserved by the
join. Since the join creates an output row for each left input row, we only
need that many rows from that input. We can only do this if the limit ordering
refers only to the left input columns. We check that the cardinality of the
left input is more than the limit, to prevent repeated applications of the
rule. We also check that the left input has no outer columns to avoid
interfering with decorrelation.

Why can we only match InnerJoins and LeftJoins (e.g. not FullJoins)?

CREATE TABLE t_x (x INT PRIMARY KEY)
CREATE TABLE t_r (r INT NOT NULL REFERENCES t_x(x))

SELECT * FROM t_r FULL JOIN t_x ON r = x LIMIT 10
vs
SELECT * FROM (SELECT * FROM t_r LIMIT 10) FULL JOIN t_x ON r = x LIMIT 10

In the first query, all rows from t_r (left rows) would have a chance to match
with a row from t_x. In the second query, left rows that otherwise would have
matched may be filtered out by the limit. Rows from t_x would then no longer
have matches, and would be outputted by the FullJoin with the left side
(t_r columns) null-extended. Therefore, pushing the limit into a join input
that may be null-extended (either input of a FullJoin) can lead to output rows
being replaced with null values.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoJoinLeft`, not the other rules in that file):

```
# PushLimitIntoJoinLeft pushes a Limit into the left input of an InnerJoin or
# LeftJoin when rows from the left input are guaranteed to be preserved by the
# join. Since the join creates an output row for each left input row, we only
# need that many rows from that input. We can only do this if the limit ordering
# refers only to the left input columns. We check that the cardinality of the
# left input is more than the limit, to prevent repeated applications of the
# rule. We also check that the left input has no outer columns to avoid
# interfering with decorrelation.
#
# Why can we only match InnerJoins and LeftJoins (e.g. not FullJoins)?
#
#   CREATE TABLE t_x (x INT PRIMARY KEY)
#   CREATE TABLE t_r (r INT NOT NULL REFERENCES t_x(x))
#
#   SELECT * FROM t_r FULL JOIN t_x ON r = x LIMIT 10
# vs
#   SELECT * FROM (SELECT * FROM t_r LIMIT 10) FULL JOIN t_x ON r = x LIMIT 10
#
# In the first query, all rows from t_r (left rows) would have a chance to match
# with a row from t_x. In the second query, left rows that otherwise would have
# matched may be filtered out by the limit. Rows from t_x would then no longer
# have matches, and would be outputted by the FullJoin with the left side
# (t_r columns) null-extended. Therefore, pushing the limit into a join input
# that may be null-extended (either input of a FullJoin) can lead to output rows
# being replaced with null values.
[PushLimitIntoJoinLeft, Normalize]
(Limit
    $input:(InnerJoin | LeftJoin
            $left:* & ^(HasOuterCols $left)
            $right:*
            $on:*
            $private:*
        ) &
        (JoinPreservesLeftRows $input)
    $limitExpr:(Const $limit:*) &
        (IsPositiveInt $limit) &
        (CanRepresentMaxRows $limit) &
        ^(LimitGeMaxRows $limit $left)
    $ordering:* &
        (OrderingCanProjectCols
            $ordering
            $cols:(OutputCols $left)
        )
)
=>
(Limit
    ((OpName $input)
        (Limit $left $limitExpr (PruneOrdering $ordering $cols))
        $right
        $on
        $private
    )
    $limitExpr
    $ordering
)
```
