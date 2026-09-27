# HoistUnboundFilterFromExistsSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 40  **Verification rounds used:** 1
**Scope detail:** the outer-bound condition is modeled as a global (0-ary) boolean symbol rather than a genuinely correlated reference into the outer row (RuleScript has no correlated-subquery-inside-EXISTS construct), which is a faithful narrower instance since the identity EXISTS(sigma_{c AND phi}(R)) = c AND EXISTS(sigma_phi(R)) holds for any c not depending on R's rows, regardless of what c itself depends on


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

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

## Independent verifier review

**Verdict:** CONFIRMED (manual)

Manually investigated by Claude (the automated run exhausted both pool attempts). Reused the custom local Exists RexRN (RexSubQuery.exists, no shared DSL changes) from EliminateExistsProject/EliminateExistsZeroRows. The rule pulls a filter conjunct out of an EXISTS subquery's own filter list when that conjunct doesn't depend on the subquery's own columns (only on the outer row) — a standard sound identity: EXISTS(sigma_{c AND phi}(R)) == c AND EXISTS(sigma_phi(R)) for any c independent of R's rows, since if c is false both sides are false (LHS: c false makes every row's filter false regardless of phi, so R's filtered set is empty), and if c is true both sides reduce to EXISTS(sigma_phi(R)). Modeled the outer-bound condition c as a global (0-ary) boolean symbol — the same symbol appearing in the subquery's filter (before) and pulled out to the outer filter (after) — rather than a genuinely correlated reference into the outer row (RuleScript has no correlated-EXISTS-subquery construct), which is a faithful narrower instance since the identity holds for ANY c independent of the inner relation, regardless of what c itself depends on. QED proves this with real SMT engagement. Verified non-vacuous with a negative control: dropping the pulled-out conjunct from after() correctly breaks the proof (provable=false).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7143167
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6540375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 59792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 422750
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 14750625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6597084
  },
  "total_duration": {
    "secs": 0,
    "nanos": 23897833
  }
}
```
