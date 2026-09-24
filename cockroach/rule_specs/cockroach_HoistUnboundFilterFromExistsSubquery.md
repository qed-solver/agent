# Name: HoistUnboundFilterFromExistsSubquery
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistUnboundFilterFromExistsSubquery pulls a filter condition out of an
Exists subquery if the filter condition only depends on columns from the
outer query. This is useful because it allows other optimization rules to
apply to the filter which was previously hidden inside the subquery.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistUnboundFilterFromExistsSubquery`, not the other rules in that file):

```
# HoistUnboundFilterFromExistsSubquery pulls a filter condition out of an
# Exists subquery if the filter condition only depends on columns from the
# outer query. This is useful because it allows other optimization rules to
# apply to the filter which was previously hidden inside the subquery.
[HoistUnboundFilterFromExistsSubquery, Normalize]
(Select
    $input:* & (CanHoistUnboundFilterFromExistsSubquery)
    $filters:[
        ...
        $item:(FiltersItem
            (Exists
                (Select
                    $innerInput:*
                    $innerFilters:[
                        ...
                        $innerItem:(FiltersItem $unboundCond:*) &
                            (IsBoundBy
                                $innerItem
                                $inputCols:(OutputCols $input)
                            )
                        ...
                    ]
                )
                $existsPrivate:*
            )
        )
        ...
    ]
)
=>
(Select
    $input
    (AppendFiltersItem
        (ReplaceFiltersItem
            $filters
            $item
            (Exists
                (Select
                    $innerInput
                    (RemoveFiltersItem $innerFilters $innerItem)
                )
                $existsPrivate
            )
        )
        $unboundCond
    )
)
```
