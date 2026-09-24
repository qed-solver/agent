# Name: ExtractRedundantConjunct
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

ExtractRedundantConjunct matches an OR expression in which the same conjunct
appears in both the left and right OR conditions:

A OR (A AND B)          =>  A
(A AND B) OR (A AND C)  =>  A AND (B OR C)

In both these cases, the redundant conjunct is A.

This transformation is useful for finding a conjunct that can be pushed down
in the query tree. For example, if the redundant conjunct A is fully bound by
one side of a join, it can be pushed through the join, even if B AND C cannot.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `ExtractRedundantConjunct`, not the other rules in that file):

```
# ExtractRedundantConjunct matches an OR expression in which the same conjunct
# appears in both the left and right OR conditions:
#
#   A OR (A AND B)          =>  A
#   (A AND B) OR (A AND C)  =>  A AND (B OR C)
#
# In both these cases, the redundant conjunct is A.
#
# This transformation is useful for finding a conjunct that can be pushed down
# in the query tree. For example, if the redundant conjunct A is fully bound by
# one side of a join, it can be pushed through the join, even if B AND C cannot.
[ExtractRedundantConjunct, Normalize]
(Or
    $left:^(Or)
    $right:^(Or) &
        (Let
            ($conjunct $ok):(FindRedundantConjunct $left $right)
            $ok
        )
)
=>
(ExtractRedundantConjunct $conjunct $left $right)
```
