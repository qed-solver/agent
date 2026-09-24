# Name: TryRemapSelectOuterCols
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryRemapSelectOuterCols is similar to TryRemapJoinOuterColsRight, but it
applies to the input of a Select.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryRemapSelectOuterCols`, not the other rules in that file):

```
# TryRemapSelectOuterCols is similar to TryRemapJoinOuterColsRight, but it
# applies to the input of a Select.
[TryRemapSelectOuterCols, Normalize]
(Select
    $input:* & (HasOuterCols $input)
    $on:* &
        (CanMaybeRemapOuterCols $input $on) &
        (Let ($remapped $ok):(TryRemapOuterCols $input $on) $ok)
)
=>
(Select $remapped $on)
```
