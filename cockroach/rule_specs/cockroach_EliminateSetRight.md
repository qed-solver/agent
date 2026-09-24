# Name: EliminateSetRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/set.opt

EliminateSetRight replaces a UnionAll operator with a left side having a
cardinality of zero, with just the right side operand. Note that it only
applies to UnionAll operators because Except operators only output left input
rows.

See the comment above EliminateSetLeft which describes when columns are
projected vs. passed-through.

Extracted from `set.opt` (which defines multiple rules — implement specifically `EliminateSetRight`, not the other rules in that file):

```
# EliminateSetRight replaces a UnionAll operator with a left side having a
# cardinality of zero, with just the right side operand. Note that it only
# applies to UnionAll operators because Except operators only output left input
# rows.
#
# See the comment above EliminateSetLeft which describes when columns are
# projected vs. passed-through.
[EliminateSetRight, Normalize]
(UnionAll $left:* & (HasZeroRows $left) $right:* $colmap:*)
=>
(Project
    $right
    (ProjectColMapRight $colmap)
    (ProjectPassthroughRight $colmap)
)
```
