# Name: TryDecorrelateSelect
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateSelect "pushes down" the join apply into the select operator,
in order to eliminate any correlation between the select filter list and the
left side of the join, and also to keep "digging" down to find and eliminate
other unnecessary correlation. Eventually, the hope is to trigger the
DecorrelateJoin pattern to turn JoinApply operators into non-apply Join
operators.

Note that citation [3] doesn't directly contain this identity, since it
assumes that the Select will be hoisted above the Join rather than becoming
part of its On condition. PushFilterIntoJoinRight allows the condition to be
pushed down, so this rule can correctly pull it up.

Citations: [3] (see identity #3)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateSelect`, not the other rules in that file):

```
# TryDecorrelateSelect "pushes down" the join apply into the select operator,
# in order to eliminate any correlation between the select filter list and the
# left side of the join, and also to keep "digging" down to find and eliminate
# other unnecessary correlation. Eventually, the hope is to trigger the
# DecorrelateJoin pattern to turn JoinApply operators into non-apply Join
# operators.
#
# Note that citation [3] doesn't directly contain this identity, since it
# assumes that the Select will be hoisted above the Join rather than becoming
# part of its On condition. PushFilterIntoJoinRight allows the condition to be
# pushed down, so this rule can correctly pull it up.
#
# Citations: [3] (see identity #3)
[TryDecorrelateSelect, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (Select $input:* $filters:*)
    $on:*
    $private:*
)
=>
((OpName) $left $input (ConcatFilters $on $filters) $private)
```
