# Name: CollapseRepeatedLikePatternWildcards
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

CollapseLikePatternWildcards collapses repeated '%' wildcards into a single
'%' in the pattern of a LIKE expression.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `CollapseRepeatedLikePatternWildcards`, not the other rules in that file):

```
# CollapseLikePatternWildcards collapses repeated '%' wildcards into a single
# '%' in the pattern of a LIKE expression.
[CollapseRepeatedLikePatternWildcards, Normalize]
(Like | NotLike | ILike | NotILike
    $input:*
    $pattern:* &
        (Let
            (
                $collapsed
                $ok
            ):(CollapseRepeatedLikePatternWildcards $pattern)
            $ok
        )
)
=>
((OpName) $input $collapsed)
```
