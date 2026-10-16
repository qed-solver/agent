# CollapseRepeatedLikePatternWildcards

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

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

The rule's sole semantic content is the glob-theory fact that consecutive '%' wildcards are equivalent to one ('x LIKE a%%b' ⟺ 'x LIKE a%b'), which is a statement about the internal string-pattern-matching semantics of the LIKE operator. QED reasons over an equality-plus-bags theory with all predicates (including LIKE) as uninterpreted symbols and no string sort or pattern axioms, so distinct pattern constants produce independent uninterpreted atoms that the SMT solver can refute with a countermodel; since the prover is the fixed Rust arbiter that treats every operator name as uninterpreted, no RuleScript encoding or extend_dsl_file extension can supply the missing '%' idempotence axiom. This is exactly the "operator whose specific internal semantics QED cannot see through as an uninterpreted function" limitation, matching the previously skipped LikeSimplification and NotLikeToNotLike rules, and no non-trivial special case avoids it. ```
