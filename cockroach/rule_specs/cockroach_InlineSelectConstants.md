# Name: InlineSelectConstants
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/inline.opt

InlineSelectConstants finds variable references in Filters expressions that
refer to constant input values, and then inlines those constant values in
place of the corresponding variable references. This sometimes allows further
simplifications such as constant folding or generation of constrained scans.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineSelectConstants`, not the other rules in that file):

```
# InlineSelectConstants finds variable references in Filters expressions that
# refer to constant input values, and then inlines those constant values in
# place of the corresponding variable references. This sometimes allows further
# simplifications such as constant folding or generation of constrained scans.
[InlineSelectConstants, Normalize]
(Select
    $input:* &
        ^(ColsAreEmpty
            $constCols:(FindInlinableConstants $input)
        )
    $filters:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
)
=>
(Select
    $input
    (InlineFilterConstants $filters $input $constCols)
)
```
