# PushLimitIntoJoinLeft

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

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

The rule's soundness rests entirely on ordering/list semantics — that Limit(A, n, ord) keeps A's first n rows in ord, that the join's output preserves left-row order so the outer limit's prefix rows all come from within the pushed-down limit's rows, and that the join preserves left rows — none of which QED can model, since it only decides bag-semantic equivalence where Sort/Limit/Offset have no meaning. Substituting an uninterpreted operator (or an uninterpreted left-only predicate) for the limit cannot salvage it: with Limit uninterpreted the two sides are different applications of opaque functions and SMT will refute them, and with a predicate p it would only prove σ_p(L⋈R) = σ_p(L)⋈R, which holds for every p and therefore verifies nothing about what a limit actually does. Extending the DSL can't close the gap either — JSONSerializer already serializes LogicalSort with limit/offset, so the missing semantics live in the unmodifiable prover itself — leaving no non-trivial special case (the only "special case" where the limit is a no-op reduces to a vacuous identity that is also inexpressible as a premise) for QED to decide. ```
