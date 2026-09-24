# Name: InlineJoinConstantsRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/inline.opt

InlineJoinConstantsRight finds variable references in a join condition that
refers to constant values projected by the right input. It then inlines those
constant values in place of the corresponding variable references. This
sometimes allows further simplifications such as constant folding or filter
pushdown.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineJoinConstantsRight`, not the other rules in that file):

```
# InlineJoinConstantsRight finds variable references in a join condition that
# refers to constant values projected by the right input. It then inlines those
# constant values in place of the corresponding variable references. This
# sometimes allows further simplifications such as constant folding or filter
# pushdown.
[InlineJoinConstantsRight, Normalize]
(Join
    $left:*
    $right:* &
        ^(ColsAreEmpty
            $constCols:(FindInlinableConstants $right)
        )
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
    (InlineFilterConstants $on $right $constCols)
    $private
)
```
