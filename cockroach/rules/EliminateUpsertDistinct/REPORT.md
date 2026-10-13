# EliminateUpsertDistinct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** assumes the UpsertDistinctOn's input is a base table whose single grouping column is a lax key (declared unique but nullable, so at most one NULL row) and there are no aggregate output columns, so the UpsertDistinctOn is a plain group-by on that column and the replacement Project just projects it.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateUpsertDistinct is similar to EliminateDistinct, but it only checks if
the grouping columns are a lax key because UpsertDistinctOn considers NULL
values to be distinct from one another for the purposes of grouping.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateUpsertDistinct`, not the other rules in that file):

```
# EliminateUpsertDistinct is similar to EliminateDistinct, but it only checks if
# the grouping columns are a lax key because UpsertDistinctOn considers NULL
# values to be distinct from one another for the purposes of grouping.
[EliminateUpsertDistinct, Normalize]
(UpsertDistinctOn | EnsureUpsertDistinctOn
    $input:*
    $aggs:*
    $groupingPrivate:* &
        (ColsAreLaxKey (GroupingCols $groupingPrivate) $input)
)
=>
(Project $input [] (GroupingOutputCols $groupingPrivate $aggs))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-trivial (before is an Aggregate/GROUP BY, after is a plain Project) and correctly proves that a GROUP BY on a unique column is equivalent to projecting that column. It is a valid, honestly-labeled PARTIAL special case of EliminateUpsertDistinct: the unique-key precondition (strict key) is stronger than the source's lax-key requirement but implies it, and in the all-values-distinct case the UpsertDistinctOn NULLs-are-distinct semantics coincide with plain GROUP BY semantics, so the operator substitution is sound. No symbol-sharing or triviality issues are present. ```

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
    "nanos": 378125
  }
}
```
