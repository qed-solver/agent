# HoistUnboundJoinFilterFromExistsSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 40  **Verification rounds used:** 1
**Scope detail:** INNER join only, the outer-bound condition is modeled as a global (0-ary) boolean symbol rather than a genuinely correlated reference into the outer row (RuleScript has no correlated-subquery-inside-EXISTS construct), which is a faithful narrower instance of the same identity as HoistUnboundFilterFromExistsSubquery, applied to a join's filter list instead of a plain Select's


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

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
```

## Independent verifier review

**Verdict:** CONFIRMED

Manually investigated by Claude (the automated run exhausted both pool attempts). Same identity and technique as HoistUnboundFilterFromExistsSubquery, applied to a join's own filter list (inside the EXISTS subquery) instead of a plain Select's: EXISTS(Join(L, R, c AND phi)) == EXISTS(Join(L, R, phi)) AND c, for c independent of L/R's rows (modeled as a global 0-ary boolean symbol, same faithful-narrower-instance rationale as the sibling rule — the identity holds for any c independent of the inner join, regardless of what c itself depends on). Reused the custom local Exists RexRN (no shared DSL changes). QED proves this with real SMT engagement; verified non-vacuous with a negative control (dropping the pulled-out conjunct from after() correctly breaks the proof).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8641626
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6901792
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 87292
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 593292
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 17750167
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6996333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 28015416
  }
}
```
