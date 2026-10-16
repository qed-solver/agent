# PushLimitIntoJoinLeft

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 29  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on ordered top-N semantics — that the outer Limit's selected rows must come from within the first-n left-input rows under the join's preservation guarantee — but QED models queries under pure bag semantics and treats Limit/Sort as uninterpreted QOps with no row-count, ordering, or top-N axioms (qed.pdf §6.2), so the before-side Limit(Join(L,R)) and after-side Limit(Join(Limit(L),R)) are structurally distinct uninterpreted applications that SMT can never equate; no non-vacuous special case reduces to a bag equality (the rule's own guards explicitly exclude the degenerate limit≥|L| case), and the gap is in the trusted prover's model rather than a missing DSL builder, since JSONSerializer already serializes LogicalSort's offset/limit fields which QED treats opaquely. ```
