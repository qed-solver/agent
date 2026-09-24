# Name: SimplifyAndTrue
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyAndTrue simplifies the And operator by discarding a True condition on
the right side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyAndTrue`, not the other rules in that file):

```
# SimplifyAndTrue simplifies the And operator by discarding a True condition on
# the right side.
[SimplifyAndTrue, Normalize]
(And $left:* (True))
=>
$left
```
