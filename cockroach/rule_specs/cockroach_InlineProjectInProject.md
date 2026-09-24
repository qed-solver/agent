# Name: InlineProjectInProject
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/inline.opt

InlineProjectInProject folds an inner Project operator into an outer Project
that references each inner synthesized column no more than one time. If there
are no duplicate references, then there's no benefit to keeping the multiple
nested projections. This rule simplifies the relational expression tree and
makes it more likely that other normalization rules will match.

This rule is low priority so that it runs after the MergeProjects rule, since
that rule is cheaper to match and replace.

Example:
SELECT x2*2 FROM (SELECT x+1 AS x2 FROM xy)
=>
SELECT (x+1)*2 FROM xy

Extracted from `inline.opt` (which defines multiple rules — implement specifically `InlineProjectInProject`, not the other rules in that file):

```
# InlineProjectInProject folds an inner Project operator into an outer Project
# that references each inner synthesized column no more than one time. If there
# are no duplicate references, then there's no benefit to keeping the multiple
# nested projections. This rule simplifies the relational expression tree and
# makes it more likely that other normalization rules will match.
#
# This rule is low priority so that it runs after the MergeProjects rule, since
# that rule is cheaper to match and replace.
#
# Example:
#   SELECT x2*2 FROM (SELECT x+1 AS x2 FROM xy)
#   =>
#   SELECT (x+1)*2 FROM xy
#
[InlineProjectInProject, Normalize, LowPriority]
(Project
    $input:(Project * $innerProjections:*)
    $projections:*
    $passthrough:* &
        ^(HasDuplicateRefs
            $projections
            $passthrough
            (ProjectionCols $innerProjections)
        )
)
=>
(InlineProjectProject $input $projections $passthrough)
```
