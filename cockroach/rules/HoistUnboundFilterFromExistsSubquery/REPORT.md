# HoistUnboundFilterFromExistsSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 0  **Verification rounds used:** 0
**Scope detail:** an EXISTS filter over a subquery whose filter has exactly one outer-only conjunct (an uninterpreted predicate of the first outer column) and one inner-only conjunct, over single-column relations


## Source rule (as given to the porter)

```
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
```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7472374
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 7212833
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 62792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 436083
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16375834
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7264292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 26372916
  }
}
```
