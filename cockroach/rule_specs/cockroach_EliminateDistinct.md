# Name: EliminateDistinct
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateDistinct discards a DistinctOn operator that is eliminating duplicate
rows by using grouping columns that are statically known to form a strict key.
By definition, a strict key does not allow duplicate values, so the GroupBy is
redundant and can be eliminated.

Since a DistinctOn operator can serve as a projection operator, we need to
replace it with a Project so that the correct columns are projected. The
project itself may be eliminated later by other rules.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateDistinct`, not the other rules in that file):

```
# EliminateDistinct discards a DistinctOn operator that is eliminating duplicate
# rows by using grouping columns that are statically known to form a strict key.
# By definition, a strict key does not allow duplicate values, so the GroupBy is
# redundant and can be eliminated.
#
# Since a DistinctOn operator can serve as a projection operator, we need to
# replace it with a Project so that the correct columns are projected. The
# project itself may be eliminated later by other rules.
[EliminateDistinct, Normalize]
(DistinctOn | EnsureDistinctOn
    $input:*
    $aggs:*
    $groupingPrivate:* &
        (ColsAreStrictKey (GroupingCols $groupingPrivate) $input)
)
=>
(Project $input [] (GroupingOutputCols $groupingPrivate $aggs))
```
