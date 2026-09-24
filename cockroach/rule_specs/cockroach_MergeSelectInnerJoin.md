# Name: MergeSelectInnerJoin
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

MergeSelectInnerJoin merges a Select operator with an InnerJoin input by
AND'ing the filter conditions of each and creating a new InnerJoin with that
On condition. This is only safe to do with InnerJoin in the general case
where the conditions could filter either left or right rows. The special case
where a condition filters only one or the other is already taken care of by
the PushSelectIntoJoin rules.
NOTE: Keep this rule ordered before the PushSelectIntoJoin rules to avoid
missing out on the potential for new filter inference based on
equivalent columns.

Extracted from `select.opt` (which defines multiple rules — implement specifically `MergeSelectInnerJoin`, not the other rules in that file):

```
# MergeSelectInnerJoin merges a Select operator with an InnerJoin input by
# AND'ing the filter conditions of each and creating a new InnerJoin with that
# On condition. This is only safe to do with InnerJoin in the general case
# where the conditions could filter either left or right rows. The special case
# where a condition filters only one or the other is already taken care of by
# the PushSelectIntoJoin rules.
# NOTE: Keep this rule ordered before the PushSelectIntoJoin rules to avoid
#       missing out on the potential for new filter inference based on
#       equivalent columns.
[MergeSelectInnerJoin, Normalize]
(Select
    $input:(InnerJoin | InnerJoinApply
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:*
)
=>
((OpName $input)
    $left
    $right
    (ConcatFilters $on $filters)
    $private
)
```
