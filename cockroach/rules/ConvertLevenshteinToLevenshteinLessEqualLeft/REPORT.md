# ConvertLevenshteinToLevenshteinLessEqualLeft

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

ConvertLevenshteinToLevenshteinLessEqualLeft converts a comparison of an
integer and the result of a levenshtein function into a comparison with a
levenshtein_less_equal function.

levenshtein('foo', 'bar') < 5
=>
levenshtein_less_equal('foo', 'bar', 5) < 5

levenshtein_less_equal is more efficient than levenshtein because it returns
early once the distance exceeds the given max distance.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `ConvertLevenshteinToLevenshteinLessEqualLeft`, not the other rules in that file):

```
# ConvertLevenshteinToLevenshteinLessEqualLeft converts a comparison of an
# integer and the result of a levenshtein function into a comparison with a
# levenshtein_less_equal function.
#
#   levenshtein('foo', 'bar') < 5
#   =>
#   levenshtein_less_equal('foo', 'bar', 5) < 5
#
# levenshtein_less_equal is more efficient than levenshtein because it returns
# early once the distance exceeds the given max distance.
[ConvertLevenshteinToLevenshteinLessEqualLeft, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Function $args:* $private:(FunctionPrivate "levenshtein"))
    $right:* &
        (IsInt $right) &
        (Let ($arg1 $arg2 $ok):(ScalarPair $args) $ok)
)
=>
((OpName)
    (MakeLevenshteinLessEqualFunction $arg1 $arg2 $right)
    $right
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests entirely on the backend's clamp identity `levenshtein_less_equal(a,b,n) = min(levenshtein(a,b), n)`, but RuleScript can only introduce these as independent uninterpreted symbols and offers no way to state any relationship between them — RexRN has no equality/arithmetic/min, there are no function-axiom mechanisms, and a scan's "guaranteed" constraint would itself be an uninterpreted predicate over columns that cannot bridge two distinct function terms. Since QED must prove equivalence for *all* instantiations of uninterpreted symbols and the fixed prover has no path for predicate inference between independent symbols, no genuine encoding is provable — this is precisely the "bespoke internal semantics of a backend operator" limitation.
