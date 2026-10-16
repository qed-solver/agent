# PushLimitIntoOffset

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 33  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

PushLimitIntoOffset pushes the Limit operator into the offset. This should
not have a negative impact but it would allow the use of the GenerateLimitedScans
rule.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushLimitIntoOffset`, not the other rules in that file):

```
# PushLimitIntoOffset pushes the Limit operator into the offset. This should
# not have a negative impact but it would allow the use of the GenerateLimitedScans
# rule.
[PushLimitIntoOffset, Normalize]
(Limit
    (Offset
        $input:*
        $offsetExpr:(Const $offset:* & (IsPositiveInt $offset))
        $offsetOrdering:*
    )
    (Const $limit:* & (IsPositiveInt $limit))
    $limitOrdering:* &
        (IsSameOrdering $offsetOrdering $limitOrdering) &
        (CanAddConstInts $limit $offset)
)
=>
(Offset
    (Limit $input (AddConstInts $offset $limit) $limitOrdering)
    $offsetExpr
    $offsetOrdering
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on order- and cardinality-based semantics: which rows `Limit` keeps depends on which rows come first after `Offset` skips `off` of them, so `Limit(Offset(X, off), lim) ≡ Offset(Limit(X, off+lim), off)` holds only under row ordering. QED's bag semantics models no `Sort`/`Limit`/`Offset` operators and carries no axioms about row order or row counts (and the DSL exposes no builders for them either), so the two distinct nestings are not provably equivalent — any RuleScript encoding must drop both wrappers, collapsing before and after to the vacuous identity `X = X`, a genuine prover-side limitation that extending `RelRN.java` cannot close since the QED prover itself cannot be modified to add ordering/counting axioms. ```
