# Name: SimplifyFalseAnd
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyFalseAnd maps the And operator to False if its left input is False.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyFalseAnd`, not the other rules in that file):

```
# SimplifyFalseAnd maps the And operator to False if its left input is False.
[SimplifyFalseAnd, Normalize]
(And $left:(False) *)
=>
$left
```
