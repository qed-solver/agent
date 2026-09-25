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

The rewrite `x OP levenshtein(s,t) ⟺ x OP levenshtein_less_equal(s,t,x)` is only valid because of CockroachDB's internal clamping contract for `levenshtein_less_equal` (returns the true distance when it is ≤ the bound, and a value strictly greater than the bound otherwise) — and checking each of Eq/Ge/Gt/Le/Lt, every one of the five directions of the equivalence depends on that relationship, which is an entailment between two function symbols that QED models as independent uninterpreted functions. RuleScript's core language offers no arithmetic or conditional terms and no axiom/assume mechanism to state such a contract, and since the frozen prover itself never relates independent uninterpreted symbols, no DSL builder extension could communicate it either — so no general or special-cased encoding (even with a literal bound, which would still leave the predicate and both function symbols uninterpreted) is provable. ```
