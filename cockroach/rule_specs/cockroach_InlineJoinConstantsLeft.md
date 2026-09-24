# Name: InlineJoinConstantsLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/inline.opt

InlineJoinConstantsLeft finds variable references in a join condition that
refers to constant values projected by the left input. It then inlines those
constant values in place of the corresponding variable references. This
sometimes allows further simplifications such as constant folding or filter
pushdown.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineJoinConstantsLeft`, not the other rules in that file):

```
# InlineJoinConstantsLeft finds variable references in a join condition that
# refers to constant values projected by the left input. It then inlines those
# constant values in place of the corresponding variable references. This
# sometimes allows further simplifications such as constant folding or filter
# pushdown.
[InlineJoinConstantsLeft, Normalize]
(Join
    $left:* &
        ^(ColsAreEmpty $constCols:(FindInlinableConstants $left))
    $right:*
    $on:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
    $private:* & (NoJoinHints $private)
)
=>
((OpName)
    $left
    $right
    (InlineFilterConstants $on $left $constCols)
    $private
)
```
