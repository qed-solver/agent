# Name: PushLimitIntoLock
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

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
