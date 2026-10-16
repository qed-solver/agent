# HoistUnboundJoinFilterFromExistsSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 102  **Verification rounds used:** 6
**Scope detail:** the exists subquery's correlated join is encoded as a SEMI join between the single-column outer input and a single inner table, with one uninterpreted outer-only conjunct and one uninterpreted conjunct over both sides


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

The encoding is non-trivial and symbol-correct: the same uninterpreted predicate "P" is shared between the pre-hoist join condition (via joinField(0)) and the post-hoist outer filter, and the structural difference (P moved from the SEMI-join ON into a pre-filter on the outer input) is exactly the rule's transformation, which the proof establishes under QED's bag/3VL semantics (∃.P∧R ≡ P∧∃.R, valid across true/false/null cases). The specialization is real but genuine and honestly disclosed in the SCOPE line: RuleScript has no EXISTS/subquery operator (a DSL gap, not a QED one), so flattening EXISTS-over-join to a SEMI join with a single inner table and one conjunct per side is a semantically faithful reduction, and the SEMI join kind matches the rule's existential semantics (consistent with HoistSelectExists); the proof covers the rule's core logical identity in a specific, non-degenerate tree shape rather than all four join kinds and filter lists, which the PARTIAL tag accurately conveys. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8547875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36143333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1099792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 546167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21996833
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36263250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 73868541
  }
}
```
