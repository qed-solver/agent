# Name: FoldColumnAccess
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldColumnAccess eliminates a column access operator applied to a tuple value
that is statically constructed, like this:

(((i, i+1) as foo, bar)).foo
(((1, 2) as foo, bar)).bar

The rule replaces the column access operator with the referenced tuple
element.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldColumnAccess`, not the other rules in that file):

```
# FoldColumnAccess eliminates a column access operator applied to a tuple value
# that is statically constructed, like this:
#
#   (((i, i+1) as foo, bar)).foo
#   (((1, 2) as foo, bar)).bar
#
# The rule replaces the column access operator with the referenced tuple
# element.
[FoldColumnAccess, Normalize]
(ColumnAccess
    $input:*
    $idx:* &
        (Let ($result $ok):(FoldColumnAccess $input $idx) $ok)
)
=>
$result
```
