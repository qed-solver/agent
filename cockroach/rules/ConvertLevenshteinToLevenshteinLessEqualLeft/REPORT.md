# ConvertLevenshteinToLevenshteinLessEqualLeft

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

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

The rule's validity rests entirely on CockroachDB's specific definition of `levenshtein_less_equal(s,t,d)` (exact distance when ≤ d, else d+1 — the porter's own Go-code reading confirmed this), i.e. an algebraic relationship between the *internals of two specific functions*. QED models `levenshtein` and `levenshtein_less_equal` as independent uninterpreted function symbols, and the core language has no arithmetic, no conditionals, and no mechanism to relate or axiomatize two function symbols, so the two sides reduce to unrelated `L(s,t) OP d` vs `LLE(s,t,d) OP d` where the SMT solver trivially finds countermodels — and no faithful special case (any of the five comparison operators) can escape the dependency on that clamping property. (The porter's logged reason was an LLM context-overflow infrastructure error rather than an analysis, but the UNSUPPORTED conclusion is the correct one.)
