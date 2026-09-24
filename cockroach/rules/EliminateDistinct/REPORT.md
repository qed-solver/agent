# EliminateDistinct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 2
**Scope detail:** assumes the DistinctOn's input is a base table whose single grouping column is statically known to be a strict key (declared unique) and there are no aggregate output columns, so the distinct is a plain group-by on that column and the replacement Project just projects it.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateDistinct discards a DistinctOn operator that is eliminating duplicate
rows by using grouping columns that are statically known to form a strict key.
By definition, a strict key does not allow duplicate values, so the GroupBy is
redundant and can be eliminated.

Since a DistinctOn operator can serve as a projection operator, we need to
replace it with a Project so that the correct columns are projected. The
project itself may be eliminated later by other rules.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateDistinct`, not the other rules in that file):

```
# EliminateDistinct discards a DistinctOn operator that is eliminating duplicate
# rows by using grouping columns that are statically known to form a strict key.
# By definition, a strict key does not allow duplicate values, so the GroupBy is
# redundant and can be eliminated.
#
# Since a DistinctOn operator can serve as a projection operator, we need to
# replace it with a Project so that the correct columns are projected. The
# project itself may be eliminated later by other rules.
[EliminateDistinct, Normalize]
(DistinctOn | EnsureDistinctOn
    $input:*
    $aggs:*
    $groupingPrivate:* &
        (ColsAreStrictKey (GroupingCols $groupingPrivate) $input)
)
=>
(Project $input [] (GroupingOutputCols $groupingPrivate $aggs))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is structurally faithful: DistinctOn is exactly a group-by with no aggregation calls (matching `Aggregate(source, [field(0)], [])`), and the RHS is a plain `Project` of the input's grouping column, and the source rule's one substantive precondition — grouping columns statically form a strict key of the input — is correctly expressed as the scan's declared `unique` non-nullable key, which is precisely what makes before/after non-vacuously different (the proof can only hold because of that key; without it a group-by over a duplicate column would not equal a plain projection). The narrowing (base-table input, single grouping column, identity projection) is forced by real QED/DSL limits — strict keys can only be declared on `scan` (a `ScanMany` cannot declare a key, and no other node exposes key declarations) — and the file's SCOPE line states exactly these assumptions, so the partial scope is honest and the result is a genuine, non-degenerate proof. ```

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
    "nanos": 340041
  }
}
```
