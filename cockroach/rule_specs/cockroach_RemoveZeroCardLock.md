# Name: RemoveZeroCardLock
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/mutation.opt

RemoveZeroCardLock removes lock operations when we know no rows will be locked.

Extracted from `mutation.opt` (which defines multiple rules — implement specifically `RemoveZeroCardLock`, not the other rules in that file):

```
# RemoveZeroCardLock removes lock operations when we know no rows will be locked.
[RemoveZeroCardLock, Normalize]
(Lock $rows:* & (HasZeroRows $rows))
=>
$rows
```
