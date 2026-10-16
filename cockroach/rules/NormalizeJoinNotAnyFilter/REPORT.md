# NormalizeJoinNotAnyFilter

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 0  **Verification rounds used:** 0
**Scope detail:** an inner join whose ON clause is the single conjunct NOT (x = ANY(sub)), with x the first left column and sub a single-column relation


## Source rule (as given to the porter)

```
# Name: NormalizeJoinNotAnyFilter
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

NormalizeJoinNotAnyFilter is similar to NormalizeSelectNotAnyFilter, except
that it operates on Not Any expressions within Join filters rather than Select
filters.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `NormalizeJoinNotAnyFilter`, not the other rules in that file):

```
# NormalizeJoinNotAnyFilter is similar to NormalizeSelectNotAnyFilter, except
# that it operates on Not Any expressions within Join filters rather than Select
# filters.
[NormalizeJoinNotAnyFilter, Normalize]
(Join
    $left:*
    $right:*
    $on:[
        ...
        $item:(FiltersItem
            (Not (Any $anyInput:* $scalar:* $anyPrivate:*))
        )
        ...
    ]
    $private:*
)
=>
((OpName)
    $left
    $right
    (ReplaceFiltersItem
        $on
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
    $private
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
    "nanos": 9561126
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 8427208
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 95083
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 636084
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18618000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 8501542
  },
  "total_duration": {
    "secs": 0,
    "nanos": 30158208
  }
}
```
