# Name: NormalizeLikeAny
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/select.opt

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
