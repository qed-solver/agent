# Name: SimplifyTrueOr
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyTrueOr maps the Or operator to True if its left input is True.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyTrueOr`, not the other rules in that file):

```
# SimplifyTrueOr maps the Or operator to True if its left input is True.
[SimplifyTrueOr, Normalize]
(Or $left:(True) *)
=>
$left
```
