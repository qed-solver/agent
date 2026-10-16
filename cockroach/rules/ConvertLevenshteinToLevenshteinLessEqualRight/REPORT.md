# ConvertLevenshteinToLevenshteinLessEqualRight

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

ConvertLevenshteinToLevenshteinLessEqualRight is the same as
ConvertLevenshteinToLevenshteinLessEqualLeft but matches comparisons with the
levenshtein function on the RHS.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `ConvertLevenshteinToLevenshteinLessEqualRight`, not the other rules in that file):

```
# ConvertLevenshteinToLevenshteinLessEqualRight is the same as
# ConvertLevenshteinToLevenshteinLessEqualLeft but matches comparisons with the
# levenshtein function on the RHS.
[ConvertLevenshteinToLevenshteinLessEqualRight, Normalize]
(Eq | Ge | Gt | Le | Lt
    $left:* & (IsInt $left)
    (Function $args:* $private:(FunctionPrivate "levenshtein")) &
        (Let ($arg1 $arg2 $ok):(ScalarPair $args) $ok)
)
=>
((OpName)
    $left
    (MakeLevenshteinLessEqualFunction $arg1 $arg2 $left)
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness rests entirely on CockroachDB's bespoke internal semantics — `levenshtein_less_equal(a,b,n) = min(levenshtein(a,b), n+1)` — a relation *between two distinct named scalar operators*; QED models each operator as an independent uninterpreted function with no axiom channel, and its scalar language has no min/conditional/arithmetic in which the clamping relation could be reconstructed, so `N cmp f(a,b) ≡ N cmp g(a,b,N)` is refutable by the solver for all five comparisons. The one remaining encoding trick — reusing the same operator name on both sides — would merely prove the false (and useless) claim that the two functions are identical. This is exactly the "bespoke operator internal semantics QED cannot see through" limitation, with no narrower special case that doesn't still require the unprovable relation. ```
