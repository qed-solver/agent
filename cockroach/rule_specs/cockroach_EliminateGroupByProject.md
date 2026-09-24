# Name: EliminateGroupByProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateGroupByProject discards a nested Project operator that is only
removing columns from its input (and not synthesizing new ones). That's
something the GroupBy operators can do on their own. This rule does not match
UpsertDistinctOn expressions because they are not built with a Project as a
child, so there is no Project to eliminate.

Note: EliminateGroupByProject should be located above
EliminateJoinUnderGroupByLeft so that it can remove any interfering Projects.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateGroupByProject`, not the other rules in that file):

```
# EliminateGroupByProject discards a nested Project operator that is only
# removing columns from its input (and not synthesizing new ones). That's
# something the GroupBy operators can do on their own. This rule does not match
# UpsertDistinctOn expressions because they are not built with a Project as a
# child, so there is no Project to eliminate.
#
# Note: EliminateGroupByProject should be located above
# EliminateJoinUnderGroupByLeft so that it can remove any interfering Projects.
[EliminateGroupByProject, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn | EnsureDistinctOn
        | EnsureUpsertDistinctOn
    $input:(Project $innerInput:*) &
        (ColsAreSubset
            (OutputCols $input)
            (OutputCols $innerInput)
        )
    $aggregations:*
    $groupingPrivate:*
)
=>
((OpName) $innerInput $aggregations $groupingPrivate)
```
