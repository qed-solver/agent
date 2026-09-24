# Name: DecorrelateProjectSet
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

DecorrelateProjectSet pulls an input relation outside of a ProjectSet if the
input is not correlated with any of the functions in the ProjectSet. The
input is then cross-joined with a new ProjectSet, which contains the same
functions but has an empty input (a unary VALUES node).

The advantage of this transformation is it means each of the functions in the
ProjectSet only need to be executed once in total, instead of once for each
input row.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `DecorrelateProjectSet`, not the other rules in that file):

```
# DecorrelateProjectSet pulls an input relation outside of a ProjectSet if the
# input is not correlated with any of the functions in the ProjectSet. The
# input is then cross-joined with a new ProjectSet, which contains the same
# functions but has an empty input (a unary VALUES node).
#
# The advantage of this transformation is it means each of the functions in the
# ProjectSet only need to be executed once in total, instead of once for each
# input row.
[DecorrelateProjectSet, Normalize]
(ProjectSet
    $input:^(Values)
    $zip:* & ^(IsZipCorrelated $zip (OutputCols $input))
)
=>
(InnerJoin
    $input
    (ProjectSet (ConstructNoColsRow) $zip)
    []
    (EmptyJoinPrivate)
)
```
