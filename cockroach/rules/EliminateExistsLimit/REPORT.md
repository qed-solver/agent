# EliminateExistsLimit

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 14  **Verification rounds used:** 2

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

The identity holds only under the scalar existential projection (EXISTS) — the two sides are not bag-equal (`LIMIT n·Q ≠ Q` as bags), and the Limit occurrence exists only on the left, so it cannot cancel as shared context the way it did in the Limit-preserving Cockroach rules. QED's bag theory gives Limit no meaning at all (per qed.pdf, Limit is only the uninterpreted QOp with no axioms — the prover's stated list-semantic limitation — and the prover itself is off-limits to modify), the core language exposes no Limit/Exists operator, and uninterpreted predicate symbols cannot tie `EXISTS(LIMIT n·S)` to `EXISTS(S)`, so even an `extend_dsl_file` attempt could only surface a Limit the prover has no axiom for; the correlated-only guard (`HasOuterCols`) is additionally a side condition a bare before/after pair cannot express.
