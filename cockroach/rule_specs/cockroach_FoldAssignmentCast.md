# Name: FoldAssignmentCast
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldAssignmentCast is similar to FoldCast, but it involves an assignment cast
operation. As with FoldCast, FoldAssignmentCast applies as long as the
evaluation would not cause an error.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldAssignmentCast`, not the other rules in that file):

```
# FoldAssignmentCast is similar to FoldCast, but it involves an assignment cast
# operation. As with FoldCast, FoldAssignmentCast applies as long as the
# evaluation would not cause an error.
[FoldAssignmentCast, Normalize]
(AssignmentCast
    $input:*
    $typ:* &
        (IsConstValueOrGroupOfConstValues $input) &
        (Let ($result $ok):(FoldAssignmentCast $input $typ) $ok)
)
=>
$result
```
