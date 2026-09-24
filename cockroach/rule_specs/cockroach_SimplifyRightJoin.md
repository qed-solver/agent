# Name: SimplifyRightJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

SimplifyRightJoin reduces a FullJoin operator to a LeftJoin operator when it's
known that every row in the join's right input will match at least one row in
the left input. This rule is symmetric with SimplifyLeftJoin; see that rule
for more details and examples.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyRightJoin`, not the other rules in that file):

```
# SimplifyRightJoin reduces a FullJoin operator to a LeftJoin operator when it's
# known that every row in the join's right input will match at least one row in
# the left input. This rule is symmetric with SimplifyLeftJoin; see that rule
# for more details and examples.
[SimplifyRightJoin, Normalize]
(FullJoin
    $left:*
    $right:*
    $on:* & (JoinFiltersMatchAllLeftRows $right $left $on)
    $private:*
)
=>
(LeftJoin $left $right $on $private)
```
