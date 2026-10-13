# PruneMutationInputCols

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** the mutation's input has exactly two columns of which only the first is needed, and the mutation's per-row behavior is an uninterpreted function of exactly that needed column (its other children — unique checks, fk checks, private data — are held fixed)


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/prune_cols.opt

PruneMutationInputCols discards input columns that are never used by the
mutation operator. This is high priority so that it runs before
UseSwapMutation, UseSwapMutationWithProjection, and
UseSwapMutationWithProjectionProjection.

Extracted from `prune_cols.opt` (which defines multiple rules — implement specifically `PruneMutationInputCols`, not the other rules in that file):

```
# PruneMutationInputCols discards input columns that are never used by the
# mutation operator. This is high priority so that it runs before
# UseSwapMutation, UseSwapMutationWithProjection, and
# UseSwapMutationWithProjectionProjection.
[PruneMutationInputCols, Normalize, HighPriority]
(Update | Upsert | Delete
    $input:*
    $uniqueChecks:*
    $fkChecks:*
    $mutationPrivate:* &
        (CanPruneCols
            $input
            $needed:(NeededMutationCols
                $mutationPrivate
                $uniqueChecks
                $fkChecks
            )
        )
)
=>
((OpName)
    (PruneCols $input $needed)
    $uniqueChecks
    $fkChecks
    $mutationPrivate
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding captures the rule's genuine soundness content — pushing a column-pruning projection below a per-row operator whose behavior depends only on the kept columns — by modeling the mutation as an uninterpreted single-column projection of exactly the needed input column, with before() = π_M(c0)(I) and after() = π_M(x0)(π_c0(I)), which are structurally distinct (a real pushdown, not an identity) and correctly share the input scan `I` and the single mutation symbol `M`. The source rule's `CanPruneCols`/`NeededMutationCols` machinery is backend meta-level (applicability/hygiene and column-set bookkeeping, not a semantic precondition), and the Update/Upsert/Delete family is properly abstracted to one uninterpreted per-row operator, with the fixed mutation data soundly absorbed into `M`. The only real restriction — fixing 2 input columns with only the first needed — is forced by the DSL (uninterpreted ops have fixed argument positions; it cannot quantify over column sets), is honestly and specifically declared in the SCOPE line, and loses no proof content, since any other (n, S) instance states the identical per-row commutation claim.

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
    "nanos": 465792
  }
}
```
