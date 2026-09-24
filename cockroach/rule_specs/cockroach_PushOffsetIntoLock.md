# Name: PushOffsetIntoLock
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/limit.opt

PushOffsetIntoLock pushes the Offset operator into its Lock input, as long as
the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
number of rows locked.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `PushOffsetIntoLock`, not the other rules in that file):

```
# PushOffsetIntoLock pushes the Offset operator into its Lock input, as long as
# the Lock does not use a SKIP LOCKED wait policy. This helps minimize the
# number of rows locked.
[PushOffsetIntoLock, Normalize]
(Offset
    (Lock $input:* $private:*) & ^(LockUsesSkipLocked $private)
    $offset:*
    $ordering:*
)
=>
(Lock (Offset $input $offset $ordering) $private)
```
