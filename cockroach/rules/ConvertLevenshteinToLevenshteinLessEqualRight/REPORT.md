# ConvertLevenshteinToLevenshteinLessEqualRight

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

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

The rule's soundness rests entirely on the backend's specific clamping invariant, `levenshtein_less_equal(a,b,d) = min(levenshtein(a,b), d+1)` — the porter's transcript shows it correctly derived this, which is exactly the relationship QED can never see. In RuleScript, `levenshtein` and `levenshtein_less_equal` can only be two distinct uninterpreted scalar symbols with no mechanism to assert any relation between them, and QED must certify bag-equivalence for *all* instantiations of uninterpreted symbols — under an arbitrary instantiation (e.g. `lle` returning a constant while `lev` doesn't) the before/after filters genuinely differ, so the prover can only ever report not-provable. No narrower special case (e.g. a constant `left`) removes this dependence, since even `5 OP lev(a,b)` ⟺ `5 OP lle(a,b,5)` requires the clamping property, so UNSUPPORTED is the correct conclusion; the real limitation is an operator's bespoke internal semantics, not a missing DSL shape. ```
