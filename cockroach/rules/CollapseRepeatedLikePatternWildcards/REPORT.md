# CollapseRepeatedLikePatternWildcards

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

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
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests entirely on LIKE's internal wildcard semantics (that a run of '%' matches the same language as a single '%'), but QED models LIKE as an uninterpreted predicate over uninterpreted values with no string theory, so it cannot relate like(x, p) to like(x, collapse(p)) for distinct pattern constants p and collapse(p); no DSL extension can close this gap because the missing piece is an interpretation of the predicate's string-matching internals, not a missing relational operator, and the prover itself cannot be modified. ```
