# Name: HoistUnboundJoinFilterFromExistsSubquery
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

HoistUnboundJoinFilterFromExistsSubquery is similar to
HoistUnboundFilterFromExistsSubquery, but it applies to a join filter.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `HoistUnboundJoinFilterFromExistsSubquery`, not the other rules in that file):

```
# HoistUnboundJoinFilterFromExistsSubquery is similar to
# HoistUnboundFilterFromExistsSubquery, but it applies to a join filter.
[HoistUnboundJoinFilterFromExistsSubquery, Normalize]
(Select
    $input:* & (CanHoistUnboundFilterFromExistsSubquery)
    $filters:[
        ...
        $item:(FiltersItem
            (Exists
                $join:(InnerJoin | InnerJoinApply | SemiJoin
                        | SemiJoinApply
                    $left:*
                    $right:*
                    $joinFilters:[
                        ...
                        $innerItem:(FiltersItem $unboundCond:*) &
                            (IsBoundBy
                                $innerItem
                                $inputCols:(OutputCols $input)
                            )
                        ...
                    ]
                    $joinPrivate:*
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
                ((OpName $join)
                    $left
                    $right
                    (RemoveFiltersItem $joinFilters $innerItem)
                    $joinPrivate
                )
                $existsPrivate
            )
        )
        $unboundCond
    )
)
```
