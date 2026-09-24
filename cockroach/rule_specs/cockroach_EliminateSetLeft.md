# Name: EliminateSetLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/set.opt

EliminateSetLeft replaces a UnionAll or ExceptAll operator with a right side
having a cardinality of zero, with just the left side operand.

It is possible for the left and right sides of the set operator to have column
IDs that are also present in the output columns of the operator, e.g. after
the SplitDisjunction exploration rule has been applied. These columns are
included as passthrough columns in the generated Project because they do not
need to be projected. All other column IDs are added to the ProjectionsExpr.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateSetLeft`, not the other rules in that file):

```
# EliminateSetLeft replaces a UnionAll or ExceptAll operator with a right side
# having a cardinality of zero, with just the left side operand.
#
# It is possible for the left and right sides of the set operator to have column
# IDs that are also present in the output columns of the operator, e.g. after
# the SplitDisjunction exploration rule has been applied. These columns are
# included as passthrough columns in the generated Project because they do not
# need to be projected. All other column IDs are added to the ProjectionsExpr.
[EliminateSetLeft, Normalize]
(UnionAll | ExceptAll
    $left:*
    $right:* & (HasZeroRows $right)
    $colmap:*
)
=>
(Project
    $left
    (ProjectColMapLeft $colmap)
    (ProjectPassthroughLeft $colmap)
)
```
