# Name: FoldNullComparisonLeft
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullComparisonLeft replaces the comparison operator with null if its
left input is null.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullComparisonLeft`, not the other rules in that file):

```
# FoldNullComparisonLeft replaces the comparison operator with null if its
# left input is null.
[FoldNullComparisonLeft, Normalize]
(Eq | Ne | Ge | Gt | Le | Lt | Like | NotLike | ILike | NotILike
        | SimilarTo | NotSimilarTo | RegMatch | NotRegMatch
        | RegIMatch | NotRegIMatch | Contains | ContainedBy
        | Overlaps | JsonExists | JsonSomeExists | JsonAllExists
    $left:(Null)
    *
)
=>
(Null (BoolType))
```
