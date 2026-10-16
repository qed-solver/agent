# ConvertLevenshteinToLevenshteinLessEqualLeft

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3

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

The rule's validity rests on the clamp/truncation semantics of `levenshtein_less_equal(a,b,m)` relative to `levenshtein(a,b)` (its value must be `d` when `d<=m` and `m+1` when `d>m` for all five comparisons to be preserved) — an algebraic relation between two distinct function symbols that QED treats as independent uninterpreted functions with no axiom mechanism to relate them (no such hook in RRule; the JSON format's only per-table "guaranteed" constraints are row-value checks on scan columns and cannot constrain projection outputs or tie an uninterpreted comparison predicate to a clamp relation; the Rust prover is unmodifiable). RexRN likewise has no ite/arithmetic/comparison constructors, so `levenshtein_less_equal` cannot be defined in terms of `levenshtein` and any faithful before/after pair using two distinct symbols admits a trivial SMT countermodel, leaving no non-trivial narrower special case — the same blocker confirmed in the independently verified AggregateReduceFunctions precedent.
