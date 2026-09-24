# Name: PushSelectIntoInlinableProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/inline.opt

PushSelectIntoInlinableProject pushes the Select operator into a Project, even
though the filter references it. This is made possible by inlining the
references to projected columns so that the Select becomes independent of the
Project, and therefore can be reordered. This normalization is important for
enabling Any filter conditions to be pushed down into scans.

This rule is low priority so that it runs after the PushSelectIntoProject
and MergeProjectProject rules, since those rules are cheaper to match and
replace.

Example:
SELECT * FROM (SELECT x+1 AS x2 FROM xy) WHERE x2=10
=>
SELECT x+1 AS x2 FROM (SELECT * FROM xy WHERE (x+1)=10)

Extracted from `inline.opt` (which defines multiple rules — implement specifically `PushSelectIntoInlinableProject`, not the other rules in that file):

```
# PushSelectIntoInlinableProject pushes the Select operator into a Project, even
# though the filter references it. This is made possible by inlining the
# references to projected columns so that the Select becomes independent of the
# Project, and therefore can be reordered. This normalization is important for
# enabling Any filter conditions to be pushed down into scans.
#
# This rule is low priority so that it runs after the PushSelectIntoProject
# and MergeProjectProject rules, since those rules are cheaper to match and
# replace.
#
# Example:
#   SELECT * FROM (SELECT x+1 AS x2 FROM xy) WHERE x2=10
#   =>
#   SELECT x+1 AS x2 FROM (SELECT * FROM xy WHERE (x+1)=10)
#
[PushSelectIntoInlinableProject, Normalize, LowPriority]
(Select
    (Project
        $input:*
        $projections:* & (CanInlineProjections $projections)
        $passthrough:*
    )
    $filters:* & ^(FilterHasCorrelatedSubquery $filters)
)
=>
(Project
    (Select $input (InlineSelectProject $filters $projections))
    $projections
    $passthrough
)
```
