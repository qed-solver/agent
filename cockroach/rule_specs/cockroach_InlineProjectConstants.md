# Name: InlineProjectConstants
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/inline.opt

InlineProjectConstants finds variable references in Projections expressions
that refer to constant input values, and then inlines those constant values
in place of the corresponding variable references. This sometimes allows
further simplifications such as constant folding or Project merging.

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineProjectConstants`, not the other rules in that file):

```
# InlineProjectConstants finds variable references in Projections expressions
# that refer to constant input values, and then inlines those constant values
# in place of the corresponding variable references. This sometimes allows
# further simplifications such as constant folding or Project merging.
[InlineProjectConstants, Normalize]
(Project
    $input:* &
        ^(ColsAreEmpty
            $constCols:(FindInlinableConstants $input)
        )
    $projections:[
        ...
        $item:* & (ColsIntersect (OuterCols $item) $constCols)
        ...
    ]
    $passthrough:*
)
=>
(Project
    $input
    (InlineProjectionConstants $projections $input $constCols)
    $passthrough
)
```
