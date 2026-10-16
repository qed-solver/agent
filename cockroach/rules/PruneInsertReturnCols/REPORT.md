# PruneInsertReturnCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** the insert's input has one column (the primary key, passed through), it has exactly two returned columns modeled as uninterpreted functions of that column, and the outer RETURNING projection uses the key plus exactly one of the returned columns, dropping the other.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneInsertReturnCols removes columns from the Insert operator's ReturnCols
set if they are not used in the RETURNING clause of the mutation.
Removing ReturnCols will then allow the PruneMutationFetchCols to be more
conservative with the fetch columns.
TODO(msirek): Mutations shouldn't need to return the primary key
columns. Investigate appropriate changes to SQL execution to accommodate this,
through #111733.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneInsertReturnCols`, not the other rules in that file):

```
# PruneInsertReturnCols removes columns from the Insert operator's ReturnCols
# set if they are not used in the RETURNING clause of the mutation.
# Removing ReturnCols will then allow the PruneMutationFetchCols to be more
# conservative with the fetch columns.
# TODO(msirek): Mutations shouldn't need to return the primary key
# columns. Investigate appropriate changes to SQL execution to accommodate this,
# through #111733.
[PruneInsertReturnCols, Normalize]
(Project
    $input:(Insert
        $innerInput:*
        $uniqueChecks:*
        $fastPathUniqueChecks:*
        $fkChecks:*
        $mutationPrivate:*
    )
    $projections:*
    $passthrough:* &
        (CanPruneMutationReturnCols
            $mutationPrivate
            $needed:(UnionCols3
                (PrimaryKeyCols (MutationTable $mutationPrivate))
                (ProjectionOuterCols $projections)
                $passthrough
            )
        )
)
=>
(Project
    ((OpName $input)
        $innerInput
        $uniqueChecks
        $fastPathUniqueChecks
        $fkChecks
        (PruneMutationReturnCols $mutationPrivate $needed)
    )
    $projections
    $passthrough
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the essential bag-semantic equivalence of `PruneInsertReturnCols` — removing a return column that the outer RETURNING projection never reads preserves the result — by modeling the insert's returned columns as uninterpreted functions of its input and dropping the unread column from the child, while keeping the key (matching the rule's `PrimaryKeyCols` requirement) and leaving the outer projection unchanged. `before()` and `after()` are structurally distinct (a 3-column vs 2-column child project feeding an identical outer `Proj`), so the proof is non-vacuous rather than a trivial `before()==after()`. The `SCOPE: PARTIAL` tag is present, accurate, and names the genuine, specific restrictions (one-column input = the passed-through PK, exactly two returned columns, the pruned one being the unread trailing column), so this is a real, non-degenerate special case rather than an over-constrained or mis-structured encoding.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 344000
  }
}
```
