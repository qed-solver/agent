# Name: EliminateLimit
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

EliminateLimit discards a Limit operator if its constant limit is greater than
or equal to the maximum number of rows that can be returned by the input. In
this case, the Limit is just a no-op, because the rows are already limited.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `EliminateLimit`, not the other rules in that file):

```
# EliminateLimit discards a Limit operator if its constant limit is greater than
# or equal to the maximum number of rows that can be returned by the input. In
# this case, the Limit is just a no-op, because the rows are already limited.
[EliminateLimit, Normalize]
(Limit
    $input:*
    (Const $limit:*) & (LimitGeMaxRows $limit $input)
)
=>
$input
```
