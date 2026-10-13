# PruneJoinRightCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** inner join whose right input has one key column (referenced by the on-clause and the outer projection) and one extra column that is pruned, with the on-clause and outer projection uninterpreted over the retained (left, key) columns


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneJoinRightCols discards columns on the right side of a join that are never
used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
which might be added during optimization, if any, to ensure those columns are
not pruned away.

The PruneCols property should prevent this rule (which pushes Project below
Join) from cycling with the TryDecorrelateProject rule (which pushes Join
below Project).

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneJoinRightCols`, not the other rules in that file):

```
# PruneJoinRightCols discards columns on the right side of a join that are never
# used. AddDerivedOnClauseConditionsFromFKContraints builds equijoin predicates
# which might be added during optimization, if any, to ensure those columns are
# not pruned away.
#
# The PruneCols property should prevent this rule (which pushes Project below
# Join) from cycling with the TryDecorrelateProject rule (which pushes Join
# below Project).
[PruneJoinRightCols, Normalize]
(Project
    $input:(Join $left:* $right:* $on:* $private:*)
    $projections:*
    $passthrough:* &
        (CanPruneCols
            $right
            $needed:(UnionCols3
                (FilterOuterCols
                    (AddDerivedOnClauseConditionsFromFKContraints
                        $on
                        $left
                        $right
                    )
                )
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    ((OpName $input)
        $left
        (PruneCols $right $needed)
        $on
        $private
    )
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core PruneJoinRightCols transformation: before() is `Project(G(L0,R0), InnerJoin(C(L0,R0), L, R(2col)))` and after() is `Project(G(L0,R0), InnerJoin(C(L0,R0), L, Project(R0,R)))` — structurally different plans where the unneeded right-side column R1 is eliminated. The uninterpreted symbols C and G are correctly shared, the field indices resolve properly to the join's flat column layout, and the SCOPE line honestly and specifically states the genuine restrictions (INNER join only, fixed 1+2 column shapes) rather than claiming full generality.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6927209
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34116333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 900959
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 543084
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18925500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34206375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68651291
  }
}
```
