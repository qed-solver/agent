# Name: EliminateExistsLimit
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

EliminateExistsLimit discards a Limit operator with a positive limit inside an
Exist operator.

The Limit operator prevents decorrelation rules from being applied. By
discarding the Limit, which is a no-op inside of Exist operators, the query
can be decorrelated into a more efficient SemiJoin or AntiJoin.

Note that this rule uses HasOuterCols to ensure that it only matches
correlated Exists subqueries. There is no need to discard limits from
non-correlated Exists subqueries. Limits are preferred for non-correlated
Exists subqueries. See ConvertUncorrelatedExistsToCoalesceSubquery above for
details.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `EliminateExistsLimit`, not the other rules in that file):

```
# EliminateExistsLimit discards a Limit operator with a positive limit inside an
# Exist operator.
#
# The Limit operator prevents decorrelation rules from being applied. By
# discarding the Limit, which is a no-op inside of Exist operators, the query
# can be decorrelated into a more efficient SemiJoin or AntiJoin.
#
# Note that this rule uses HasOuterCols to ensure that it only matches
# correlated Exists subqueries. There is no need to discard limits from
# non-correlated Exists subqueries. Limits are preferred for non-correlated
# Exists subqueries. See ConvertUncorrelatedExistsToCoalesceSubquery above for
# details.
[EliminateExistsLimit, Normalize]
(Exists
    (Limit
        $input:* & (HasOuterCols $input)
        (Const $limit:*) & (IsPositiveInt $limit)
    )
    $existsPrivate:*
)
=>
(Exists $input $existsPrivate)
```
