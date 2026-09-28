# NormalizeLikeAny

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 29  **Verification rounds used:** 2

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

The rule's entire semantic content is the axiom that the pattern '%' matches every non-NULL string, i.e. that `LIKE(x, '%')` ≡ `x IS NOT NULL`. In QED's translation both expressions become independent uninterpreted predicate symbols over the same scan, and QED's SMT theory has no string/glob reasoning to connect them — this is the explicitly stated limitation that QED "can't reason about predicate inference/entailment between independent symbols." No DSL extension can close this gap because the missing knowledge is a theory axiom in the (unmodifiable) prover, not a missing operator shape. ```
