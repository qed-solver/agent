# EliminateExistsLimit

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 27  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire content is that a positive LIMIT preserves the emptiness of its input (empty stays empty, non-empty stays non-empty), which is a list/ordering property that QED does not model — Limit can only be expressed as an uninterpreted bag→bag operator (QOp(Limit, k, ·)), and SMT reasoning cannot derive non-emptiness preservation of an uninterpreted function, so encoding EXISTS as a semi/correlate join gives before = semi(Outer, QOp(Limit,k,I)) vs after = semi(Outer, I), which is not merely unprovable but genuinely invalid under QED's uninterpreted semantics. No core-language construct can substitute for "at most k rows, non-empty iff input non-empty" (aggregates like COUNT(*) are equally uninterpreted — QED only knows bag-equality of aggregate inputs), so this is a fundamental QED limitation, not a missing builder that `extend_dsl_file` could close; adding a Limit builder would just feed the same uninterpreted operator to the prover and change nothing.
