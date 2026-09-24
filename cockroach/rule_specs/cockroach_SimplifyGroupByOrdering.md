# Name: SimplifyGroupByOrdering
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/ordering.opt

SimplifyGroupByOrdering removes redundant columns from the GroupBy operators'
input ordering.

Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
find. If a test case for EnsureDistinctOn is found, it should be added to the
match pattern.

Extracted from `ordering.opt` (which defines multiple rules — implement specifically `SimplifyGroupByOrdering`, not the other rules in that file):

```
# SimplifyGroupByOrdering removes redundant columns from the GroupBy operators'
# input ordering.
#
# Note: Doesn't match EnsureDistinctOn because test cases were too difficult to
# find. If a test case for EnsureDistinctOn is found, it should be added to the
# match pattern.
[SimplifyGroupByOrdering, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn
    $input:*
    $aggregations:*
    $groupingPrivate:* &
        (CanSimplifyGroupingOrdering $input $groupingPrivate)
)
=>
((OpName)
    $input
    $aggregations
    (SimplifyGroupingOrdering $input $groupingPrivate)
)
```
