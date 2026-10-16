# TryDecorrelateProjectSelect

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 0  **Verification rounds used:** 0
**Scope detail:** single-column L and R, one uninterpreted correlated Select filter and one uninterpreted projection of the R column, with the LeftJoinApply ON clause omitted


## Source rule (as given to the porter)

```
# Name: TryDecorrelateProjectSelect
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProjectSelect tries to decorrelate by hoisting a Select operator
that sits below a LeftJoin/Project operator combo. The Project operator itself
can't be reordered above the LeftJoin like it can in the InnerJoin case.
However, the Select filter can be merged with the LeftJoin filter, which is
enough to decorrelate in several useful cases.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProjectSelect`, not the other rules in that file):

```
# TryDecorrelateProjectSelect tries to decorrelate by hoisting a Select operator
# that sits below a LeftJoin/Project operator combo. The Project operator itself
# can't be reordered above the LeftJoin like it can in the InnerJoin case.
# However, the Select filter can be merged with the LeftJoin filter, which is
# enough to decorrelate in several useful cases.
[TryDecorrelateProjectSelect, Normalize]
(LeftJoinApply
    $left:*
    $right:(Project
        (Select
            $selectInput:*
            $filters:* &
                ^(FiltersBoundBy
                    $filters
                    (OutputCols $selectInput)
                )
        )
        $projections:*
        $passthrough:*
    )
    $on:*
    $private:*
)
=>
(Project
    ((OpName)
        $left
        (Project
            $selectInput
            $projections
            (UnionCols $passthrough (OutputCols $selectInput))
        )
        (ConcatFilters $on $filters)
        $private
    )
    []
    (OutputCols2 $left $right)
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
    "nanos": 12233667
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 12780834
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 109167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1012333
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23457083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 12918167
  },
  "total_duration": {
    "secs": 0,
    "nanos": 39652541
  }
}
```
