# Name: SimplifyRange
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

SimplifyRange simplifies a Range operator for which the input is no longer an
And expression, likely due to simplification of the And operator itself.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `SimplifyRange`, not the other rules in that file):

```
# SimplifyRange simplifies a Range operator for which the input is no longer an
# And expression, likely due to simplification of the And operator itself.
[SimplifyRange, Normalize]
(Range $input:^(And))
=>
$input
```
