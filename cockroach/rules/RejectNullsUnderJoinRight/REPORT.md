# RejectNullsUnderJoinRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** only the InnerJoin variant is modeled (InnerJoinApply/LeftJoin/LeftJoinApply/SemiJoin/AntiJoin are not), and null-rejection is restricted to a single explicit IS_NOT_NULL conjunct on the right input's column inside the ON condition


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsUnderJoinRight mirrors RejectNullsUnderJoinLeft.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsUnderJoinRight`, not the other rules in that file):

```
# RejectNullsUnderJoinRight mirrors RejectNullsUnderJoinLeft.
[RejectNullsUnderJoinRight, Normalize, LowPriority]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | AntiJoin
    $left:*
    $right:* &
        ^(ColsAreEmpty $rejectCols:(RejectNullCols $right))
    $on:* &
        ^(ColsAreEmpty
            $nullRejectedCols:(IntersectionCols
                $rejectCols
                (GetNullRejectedCols $on)
            )
        )
    $private:*
)
=>
((OpName)
    $left
    (Select $right (MakeNullRejectFilters $nullRejectedCols))
    $on
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = Join(L, R, on ∧ IS_NOT_NULL(r)) and after() = Join(L, R⇝IS_NOT_NULL(r), on ∧ IS_NOT_NULL(r)) are genuinely different plans, and their bag equivalence is exactly the source rule's transformation (add an IS NOT NULL filter under the right input, leaving $on unchanged) instantiated to a case the rule truly covers — an ON condition explicitly null-rejecting right column r (the file header lists `x IS NOT NULL` as a null-rejecting expression, and bare scans satisfy the ColsAreEmpty guards, which are filter-firing hygiene, not semantic preconditions). Symbol sharing is correct and load-bearing: the IS_NOT_NULL conjunct in the ON and the pushed-down filter serialize as the same operator applied to the same underlying column (join field 1 vs. right field 0), and if they had been independent symbols the equivalence would not hold, so the acceptance confirms the sharing rather than masking an error; the uninterpreted `on` is likewise shared unchanged as the source rule requires. The SCOPE: PARTIAL line is honest and specific — only InnerJoin (of the source's six join kinds) and a single explicit IS_NOT_NULL conjunct (vs. analysis-based implicit rejection over possibly multiple columns) — making this a genuine, non-degenerate special case whose proved claim is a true, meaningful instance of the real optimization. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6764666
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 24782917
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 861292
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 428208
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18853125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 24887709
  },
  "total_duration": {
    "secs": 0,
    "nanos": 59373708
  }
}
```
