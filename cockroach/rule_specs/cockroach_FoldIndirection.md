# Name: FoldIndirection
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldIndirection eliminates a constant array indirection operator applied to an
array with a statically known number of elements, like this:

ARRAY[i, i+1][1]
ARRAY[1, 2, 3][2]

The rule replaces the indirection operator with the referenced array element.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldIndirection`, not the other rules in that file):

```
# FoldIndirection eliminates a constant array indirection operator applied to an
# array with a statically known number of elements, like this:
#
#   ARRAY[i, i+1][1]
#   ARRAY[1, 2, 3][2]
#
# The rule replaces the indirection operator with the referenced array element.
[FoldIndirection, Normalize]
(Indirection
    $input:*
    $index:* &
        (IsConstValueOrGroupOfConstValues $index) &
        (Let ($result $ok):(FoldIndirection $input $index) $ok)
)
=>
$result
```
