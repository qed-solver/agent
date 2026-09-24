# Name: SimplifyFalseOr
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyFalseOr simplifies the Or operator by discarding a False condition on
the left side.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyFalseOr`, not the other rules in that file):

```
# SimplifyFalseOr simplifies the Or operator by discarding a False condition on
# the left side.
[SimplifyFalseOr, Normalize]
(Or (False) $right:*)
=>
$right
```
