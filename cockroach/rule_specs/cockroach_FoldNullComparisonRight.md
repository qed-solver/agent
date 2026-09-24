# Name: FoldNullComparisonRight
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

FoldNullComparisonRight replaces the comparison operator with null if its
right input is null.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNullComparisonRight`, not the other rules in that file):

```
# FoldNullComparisonRight replaces the comparison operator with null if its
# right input is null.
[FoldNullComparisonRight, Normalize]
(Eq | Ne | Ge | Gt | Le | Lt | Like | NotLike | ILike | NotILike
        | SimilarTo | NotSimilarTo | RegMatch | NotRegMatch
        | RegIMatch | NotRegIMatch | Contains | ContainedBy
        | Overlaps | JsonExists | JsonSomeExists | JsonAllExists
    *
    $right:(Null)
)
=>
(Null (BoolType))
```
