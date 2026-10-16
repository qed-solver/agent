# NormalizeSelectNotAnyFilter

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 0  **Verification rounds used:** 0
**Scope detail:** the ANY comparison is fixed to equality (NOT (x = ANY(sub)), i.e. NOT IN) over a single-column subquery, and the filter holds only the NOT ANY conjunct


## Source rule (as given to the porter)

```
# Name: NormalizeSelectNotAnyFilter
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeSelectNotAnyFilter rewrites a Not Any expression that is a top-level
conjunct in Select filters, turning it into a Not Exists expression. Not Any
can be rewritten as Not Exists in this context because a NULL return value is
treated as False by the filter.

Not Exists is more efficient than Not Any, since its null handling is much
simpler. In addition, the Not Exists can be transformed into an anti-join.

Citations: [5] (section 3.5)

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeSelectNotAnyFilter`, not the other rules in that file):

```
# NormalizeSelectNotAnyFilter rewrites a Not Any expression that is a top-level
# conjunct in Select filters, turning it into a Not Exists expression. Not Any
# can be rewritten as Not Exists in this context because a NULL return value is
# treated as False by the filter.
#
# Not Exists is more efficient than Not Any, since its null handling is much
# simpler. In addition, the Not Exists can be transformed into an anti-join.
#
# Citations: [5] (section 3.5)
[NormalizeSelectNotAnyFilter, Normalize]
(Select
    $input:*
    $filters:[
        ...
        $item:(FiltersItem
            (Not (Any $anyInput:* $scalar:* $anyPrivate:*))
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
        (Not
            (Exists
                (Select
                    $anyInput
                    [
                        (FiltersItem
                            (IsNot
                                (ConstructAnyCondition
                                    $anyInput
                                    $scalar
                                    $anyPrivate
                                )
                                (False)
                            )
                        )
                    ]
                )
                (ConvertSubToExistsPrivate $anyPrivate)
            )
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
    "nanos": 8222664
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 7484291
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 54084
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 407042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16027791
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7565250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 26235542
  }
}
```
