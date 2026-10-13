# PushLimitIntoOffset

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 31  **Verification rounds used:** 2

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

The rule's entire correctness claim is about positional row selection within a shared ordering (keeping rows at positions [offset, offset+limit) versus taking positions [0, offset+limit) and then skipping the first `offset`), which requires sequence/list semantics that QED's bag-semantic SMT model explicitly lacks — Sort/Limit/Offset carry no bag-semantic meaning, a documented failure category that this rule falls squarely in — and the RelRN builder API confirms no Sort/Limit/Offset operators exist (I checked the full source; LogicalSort appears only in the JSON serializer/deserializer, i.e. the prover's I/O, not a constructible or provable pattern). Even if `extend_dsl_file` added such builders, the prover would erase orderings rather than verify them (yielding only a vacuous bag-identity proof), and the guard's constant arithmetic (`CanAddConstInts`/`AddConstInts`) plus `IsSameOrdering` are inexpressible with uninterpreted symbols, so no non-vacuous encoding exists — a genuine QED limitation, not a porter encoding miss.
