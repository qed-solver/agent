# NormalizeLikeAny

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

NormalizeLikeAny replaces `x LIKE '%'` with `x IS NOT NULL`.

Extracted from `select.opt` (which defines multiple rules — implement specifically `NormalizeLikeAny`, not the other rules in that file):

```
# NormalizeLikeAny replaces `x LIKE '%'` with `x IS NOT NULL`.
[NormalizeLikeAny, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Like | ILike
                $left:*
                $pattern:(Const) &
                    (ConstStringEquals $pattern "%")
            )
        )
        ...
    ]
)
=>
(Select
    $input
    (ReplaceFiltersItem
        $filters
        $item
        (IsNot $left (Null (AnyType)))
    )
)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire semantic content is the equivalence `x LIKE '%' ≡ x IS NOT NULL`, which rests on string/glob theory (that the pattern '%' matches exactly the non-NULL strings). QED serializes `LIKE` to an uninterpreted predicate by name with no string/glob theory and treats `IS NOT NULL` as an independent symbol, so an SMT model can make the two sides diverge (e.g. a row where `like(x,'%')` is true while x is NULL, or a non-NULL x where `like` is false); no DSL extension can supply the missing glob axiom to the fixed Rust prover. This matches the verified precedent for the related `CollapseRepeatedLikePatternWildcards` and `LikeSimplification` rules, and no non-trivial encoding avoids the gap (a non-nullable column makes `IS NOT NULL` a tautology while leaving the uninterpreted `LIKE` unconstrained, so that degenerate case is unprovable too). ```
