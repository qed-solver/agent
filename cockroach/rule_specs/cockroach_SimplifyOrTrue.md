# Name: SimplifyOrTrue
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyOrTrue maps the Or operator to True if its right input is True.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyOrTrue`, not the other rules in that file):

```
# SimplifyOrTrue maps the Or operator to True if its right input is True.
[SimplifyOrTrue, Normalize]
(Or * $right:(True))
=>
$right
```
