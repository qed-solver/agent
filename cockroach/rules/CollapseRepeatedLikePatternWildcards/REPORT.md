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

The rule's soundness rests entirely on glob-pattern semantics of LIKE's pattern argument — that a run of `%` wildcards matches exactly the same string set as a single `%` — which is an internal string-semantics property QED cannot see, since it models predicates only as uninterpreted functions over bag semantics and its SMT backend has no string/glob theory. No DSL extension can close this gap: even with a string-literal constructor, `LIKE(x,'a%%b')` and `LIKE(x,'a%b')` remain two distinct uninterpreted function applications over which SMT can build distinguishing countermodels, and the prover itself is a fixed, unmodifiable arbiter. Hence the only encodable form reuses one predicate symbol for both sides, degenerating into a vacuous identity — a genuine fundamental limitation, not a missing operator the porter failed to find.
