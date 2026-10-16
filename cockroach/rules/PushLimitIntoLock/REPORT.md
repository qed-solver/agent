# PushLimitIntoLock

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoLock pushes the Limit operator into its Lock input, as long as
the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
number of rows locked.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoLock`, not the other rules in that file):

```
# PushLimitIntoLock pushes the Limit operator into its Lock input, as long as
# the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
# number of rows locked.
[PushLimitIntoLock, Normalize]
(Limit
    (Lock $input:* $private:*) & ^(LockUsesSkipLocked $private)
    $limit:*
    $ordering:*
)
=>
(Lock (Limit $input $limit $ordering) $private)
```
```

## Independent verifier review

**Verdict:** AGREE

The rewrite Limit(Lock(X)) ⟹ Lock(Limit(X)) is only sound because (a) a non-SKIP-LOCKED Lock is row-identity over its input — a backend-specific internal semantic that QED cannot see through, since Lock would be merely a distinct uninterpreted operator — and (b) Limit's row-count/ordering behavior, which QED explicitly does not model (Sort/Limit/Offset have no bag-semantic meaning; no Limit builder even exists in RelRN, and adding one would still leave it an axiom-free uninterpreted QOp). With both operators uninterpreted and unrelated, the claim reduces to proving two distinct uninterpreted functions commute, which the SMT solver refutes for every instantiation, and the rule's actual payoff (locking fewer rows) is a side effect invisible to bag equivalence, so no non-vacuous special case is encodable. ```
