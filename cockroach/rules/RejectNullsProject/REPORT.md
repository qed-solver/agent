# RejectNullsProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 104  **Verification rounds used:** 6
**Scope detail:** the projection consists of a single synthesized column `-a` (concrete UNARY_MINUS over the one nullable input column `a`) plus one passthrough column, and the parent select filter is exactly IS NOT NULL on the synthesized column; the general null-transmission analysis of the original rule (arbitrary projections, RejectNullCols metadata) is not modeled.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsProject adds a "col IS NOT NULL" null-rejecting filter to the input
of a project if the following conditions hold:
1. The parent Select operator rejects nulls on a synthesized (projection)
column.
2. At least one of the input columns of the projection is in the
RejectNullCols ColSet of the input of the Project.
3. The projection 'transmits' NULLS from the candidate column; if the column
is NULL, the output of the projection is NULL.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsProject`, not the other rules in that file):

```
# RejectNullsProject adds a "col IS NOT NULL" null-rejecting filter to the input
# of a project if the following conditions hold:
#   1. The parent Select operator rejects nulls on a synthesized (projection)
#      column.
#   2. At least one of the input columns of the projection is in the
#      RejectNullCols ColSet of the input of the Project.
#   3. The projection 'transmits' NULLS from the candidate column; if the column
#      is NULL, the output of the projection is NULL.
[RejectNullsProject, Normalize]
(Select
    $input:(Project
            $innerInput:*
            $projections:* &
                ^(ColsAreEmpty
                    $projectionCols:(ProjectionCols $projections)
                )
            $passthrough:*
        ) &
        ^(ColsAreEmpty $rejectNullCols:(RejectNullCols $input))
    $filters:* &
        ^(ColsAreEmpty
            $nullRejectedCols:(IntersectionCols
                (IntersectionCols
                    $projectionCols
                    $rejectNullCols
                )
                (GetNullRejectedCols $filters)
            )
        )
)
=>
(Select
    (Project
        (Select
            $innerInput
            [
                (FiltersItem
                    (IsNot
                        (NullRejectProjections
                            $projections
                            $nullRejectedCols
                            (RejectNullCols $innerInput)
                        )
                        (Null (AnyType))
                    )
                )
            ]
        )
        $projections
        $passthrough
    )
    $filters
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the structural shape of RejectNullsProject (adding an IS NOT NULL filter below a Project while retaining the parent filter) for a concrete null-strict projection (UNARY_MINUS); the proof is non-vacuous because it requires QED's null-strictness model for UNARY_MINUS (an uninterpreted function would not satisfy the equivalence), and the SCOPE line honestly and specifically documents that the general null-transmission analysis over arbitrary projections is not modeled.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5001291
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 8170500
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 45750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 351833
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 9499250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 8209583
  },
  "total_duration": {
    "secs": 0,
    "nanos": 20303666
  }
}
```
