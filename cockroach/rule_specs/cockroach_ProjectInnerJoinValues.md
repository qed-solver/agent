# Name: ProjectInnerJoinValues
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/join.opt

ProjectInnerJoinValues transforms an inner join with a single-row Values
operator to a Project operator. This allows decorrelation of e.g.:

SELECT (SELECT CASE WHEN ord.approved THEN 'Approved' ELSE '---' END)
FROM (VALUES (1, true), (2, false)) ord(id, approved)

Extracted from `join.opt` (which defines multiple rules — implement specifically `ProjectInnerJoinValues`, not the other rules in that file):

```
# ProjectInnerJoinValues transforms an inner join with a single-row Values
# operator to a Project operator. This allows decorrelation of e.g.:
#
#   SELECT (SELECT CASE WHEN ord.approved THEN 'Approved' ELSE '---' END)
#   FROM (VALUES (1, true), (2, false)) ord(id, approved)
#
[ProjectInnerJoinValues, Normalize]
(InnerJoin | InnerJoinApply
    $left:*
    $right:(Values) & (HasOneRow $right)
    $on:*
)
=>
(Select
    (Project
        $left
        (MakeProjectionsFromValues $right)
        (OutputCols $left)
    )
    $on
)
```
