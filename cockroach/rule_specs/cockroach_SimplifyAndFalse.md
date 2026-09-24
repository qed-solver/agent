# Name: SimplifyAndFalse
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyAndFalse maps the And operator to False if its right input is False.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyAndFalse`, not the other rules in that file):

```
# SimplifyAndFalse maps the And operator to False if its right input is False.
[SimplifyAndFalse, Normalize]
(And * $right:(False))
=>
$right
```
