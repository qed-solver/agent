# Name: PushLimitIntoOffset
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

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
