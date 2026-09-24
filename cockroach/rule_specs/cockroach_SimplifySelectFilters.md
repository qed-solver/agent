# Name: SimplifySelectFilters
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

SimplifySelectFilters simplifies the Filters operator in several possible
ways:
- Removes True operands
- Replaces the Filters operator with False if any operand is False, Null, or
a contradiction
- Flattens nested And operands by merging their conditions into parent
- Simplifies Or operands where one side is a Null to the other side
- Simplifies Is operands where the right side is True or False

Note that the Null handling behavior is different than the SimplifyAnd rules,
because Filters only appears as a Select or Join filter condition, both of
which treat a Null filter conjunct exactly as if it were False.

Extracted from `select.opt` (which defines multiple rules — implement specifically `SimplifySelectFilters`, not the other rules in that file):

```
# SimplifySelectFilters simplifies the Filters operator in several possible
# ways:
#   - Removes True operands
#   - Replaces the Filters operator with False if any operand is False, Null, or
#     a contradiction
#   - Flattens nested And operands by merging their conditions into parent
#   - Simplifies Or operands where one side is a Null to the other side
#   - Simplifies Is operands where the right side is True or False
#
# Note that the Null handling behavior is different than the SimplifyAnd rules,
# because Filters only appears as a Select or Join filter condition, both of
# which treat a Null filter conjunct exactly as if it were False.
[SimplifySelectFilters, Normalize, HighPriority]
(Select
    $input:*
    $filters:[
            ...
            $item:(FiltersItem
                    (And | True | False | Null | Or | Is)
                ) &
                ^(IsUnsimplifiableOr $item) &
                ^(IsUnsimplifiableIs $item)
            ...
        ] &
        ^(IsFilterFalse $filters)
)
=>
(Select $input (SimplifyFilters $filters))
```
