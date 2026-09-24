# Name: DetectJoinContradiction
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

DetectJoinContradiction replaces a Join condition with False if it detects a
contradiction in the filter.

Extracted from `join.opt` (which defines multiple rules — implement specifically `DetectJoinContradiction`, not the other rules in that file):

```
# DetectJoinContradiction replaces a Join condition with False if it detects a
# contradiction in the filter.
[DetectJoinContradiction, Normalize]
(Join
    $left:*
    $right:*
    $on:[
            ...
            $item:(FiltersItem) & (IsContradiction $item)
            ...
        ] &
        ^(IsFilterFalse $on)
    $private:*
)
=>
((OpName) $left $right [ (FiltersItem (False)) ] $private)
```
