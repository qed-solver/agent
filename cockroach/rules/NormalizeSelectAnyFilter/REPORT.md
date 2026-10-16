# NormalizeSelectAnyFilter

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 0  **Verification rounds used:** 0
**Scope detail:** the ANY comparison is fixed to equality (x = ANY(sub), i.e. IN) over a single-column subquery, and the filter holds only the ANY conjunct


## Source rule (as given to the porter)

```
# Name: NormalizeSelectAnyFilter
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeSelectAnyFilter rewrites an Any expression that is a top-level
conjunct in Select filters, turning it into an Exists expression. Any can be
rewritten as Exists in this context because a NULL return value is treated as
False by the filter.

Exists is more efficient than Any, since its null handling is much simpler. In
addition, the Exists can be transformed into a semi-join.

Citations: [5] (section 3.5)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeSelectAnyFilter`, not the other rules in that file):

```
# NormalizeSelectAnyFilter rewrites an Any expression that is a top-level
# conjunct in Select filters, turning it into an Exists expression. Any can be
# rewritten as Exists in this context because a NULL return value is treated as
# False by the filter.
#
# Exists is more efficient than Any, since its null handling is much simpler. In
# addition, the Exists can be transformed into a semi-join.
#
# Citations: [5] (section 3.5)
[NormalizeSelectAnyFilter, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Any $anyInput:* $scalar:* $anyPrivate:*)
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
        (Exists
            (Select
                $anyInput
                [
                    (FiltersItem
                        (ConstructAnyCondition
                            $anyInput
                            $scalar
                            $anyPrivate
                        )
                    )
                ]
            )
            (ConvertSubToExistsPrivate $anyPrivate)
        )
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
    "nanos": 9276793
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37963708
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1102250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 492250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23359834
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 38074375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 77046833
  }
}
```
