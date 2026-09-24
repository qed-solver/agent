# Name: TryDecorrelateProjectSet
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProjectSet "pushes down" an InnerJoinApply operator into a
ProjectSet operator, in hopes of eliminating any correlation between the
ProjectSet operator and the InnerJoinApply operator. Eventually, the
hope is to trigger the DecorrelateJoin pattern to turn JoinApply operators
into non-apply Join operators.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProjectSet`, not the other rules in that file):

```
# TryDecorrelateProjectSet "pushes down" an InnerJoinApply operator into a
# ProjectSet operator, in hopes of eliminating any correlation between the
# ProjectSet operator and the InnerJoinApply operator. Eventually, the
# hope is to trigger the DecorrelateJoin pattern to turn JoinApply operators
# into non-apply Join operators.
[TryDecorrelateProjectSet, Normalize]
(InnerJoinApply
    $left:*
    (ProjectSet $input:* $zip:*)
    $on:*
    $private:*
)
=>
(Select
    (ProjectSet (InnerJoinApply $left $input [] $private) $zip)
    $on
)
```
