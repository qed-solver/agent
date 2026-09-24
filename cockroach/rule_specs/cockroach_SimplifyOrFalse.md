# Name: SimplifyOrFalse
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyOrFalse simplifies the Or operator by discarding a False condition on
the right side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyOrFalse`, not the other rules in that file):

```
# SimplifyOrFalse simplifies the Or operator by discarding a False condition on
# the right side.
[SimplifyOrFalse, Normalize]
(Or $left:* (False))
=>
$left
```
