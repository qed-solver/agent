# PruneMutationFetchCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the mutation's input has exactly two columns of which only the first is in the needed FetchCols, and the mutation's output is an uninterpreted per-row function of exactly that needed column (its other children — unique checks, fk checks, remaining private data — are held fixed)


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneMutationFetchCols removes columns from the mutation operator's FetchCols
set if they are never used. Removing FetchCols can in turn can trigger the
PruneMutationInputCols rule, which can prune any input columns which are now
unreferenced.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneMutationFetchCols`, not the other rules in that file):

```
# PruneMutationFetchCols removes columns from the mutation operator's FetchCols
# set if they are never used. Removing FetchCols can in turn can trigger the
# PruneMutationInputCols rule, which can prune any input columns which are now
# unreferenced.
[PruneMutationFetchCols, Normalize]
(Update | Upsert | Delete
    $input:*
    $uniqueChecks:*
    $fkChecks:*
    $mutationPrivate:* &
        (CanPruneMutationFetchCols
            $mutationPrivate
            $needed:(NeededMutationFetchCols
                (OpName)
                $mutationPrivate
            )
        )
)
=>
((OpName)
    $input
    $uniqueChecks
    $fkChecks
    (PruneMutationFetchCols $mutationPrivate $needed)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (scan(I0,I1) → M(I0)) and after() (scan(I0,I1) → project(I0) → M(I0')) are structurally different and correctly share the M operator and input scan, so the proof covers the rule's genuine crux — a mutation whose output depends only on the needed FetchCols is unchanged when unneeded fetched columns are dropped — with that precondition faithfully encoded by construction (M takes only the needed column), the only provable form given QED's inability to reason about entailment between independent symbols. The exactly-two-columns/one-needed restriction is an honest, specific, minimal non-degenerate special case (declared in the SCOPE line), and the unchanged other children (unique checks, fk checks, fixed private data) are soundly absorbed into the held-fixed uninterpreted M, matching the source rewrite which alters only the FetchCols.

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
    "nanos": 372458
  }
}
```
