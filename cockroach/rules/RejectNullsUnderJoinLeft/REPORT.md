# RejectNullsUnderJoinLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** only the InnerJoin variant is modeled (InnerJoinApply/SemiJoin/SemiJoinApply are not), and null-rejection is restricted to a single explicit IS_NOT_NULL conjunct on the left input's column inside the ON condition


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/reject_nulls.opt

RejectNullsUnderJoinLeft adds "col IS NOT NULL" null-rejecting filters to the
left input of a join for each column that is both in the NullRejectCols ColSet
and is null-rejected by the join's filters. Note that a left join cannot be
matched even if its filters reject nulls on a column because left joins add
back unmatched columns to the output. RejectNullsUnderJoinLeft is low priority
to allow filters to be pushed down entirely, if possible.

Extracted from `reject_nulls.opt` (which defines multiple rules — implement specifically `RejectNullsUnderJoinLeft`, not the other rules in that file):

```
# RejectNullsUnderJoinLeft adds "col IS NOT NULL" null-rejecting filters to the
# left input of a join for each column that is both in the NullRejectCols ColSet
# and is null-rejected by the join's filters. Note that a left join cannot be
# matched even if its filters reject nulls on a column because left joins add
# back unmatched columns to the output. RejectNullsUnderJoinLeft is low priority
# to allow filters to be pushed down entirely, if possible.
[RejectNullsUnderJoinLeft, Normalize, LowPriority]
(InnerJoin | InnerJoinApply | SemiJoin | SemiJoinApply
    $left:* & ^(ColsAreEmpty $rejectCols:(RejectNullCols $left))
    $right:*
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
    (Select $left (MakeNullRejectFilters $nullRejectedCols))
    $right
    $on
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The porter's encoding is a faithful special case of RejectNullsUnderJoinLeft: the original rule fires when the ON condition null-rejects a left-input column (tracked via ColSet analysis) and pushes an IS NOT NULL filter below the join while leaving the ON condition unchanged; the porter instantiates this with the null-rejection made syntactically explicit as an IS_NOT_NULL conjunct in the ON condition (the one form QED can verify with an otherwise-uninterpreted predicate), yielding before=InnerJoin(L, R, on∧IS_NOT_NULL(l)) and after=InnerJoin(Filter(L, IS_NOT_NULL(l)), R, on∧IS_NOT_NULL(l)), which is exactly what the original rule produces in that sub-case. The proof is non-vacuous (the filter node is absent in before and present in after), the ON predicate is genuinely uninterpreted, the join kind and filter placement match the source rule, and the SCOPE tag honestly names both restrictions (InnerJoin-only; single explicit IS_NOT_NULL conjunct). ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7044958
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 24856666
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 827167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 399458
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19651459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25003500
  },
  "total_duration": {
    "secs": 0,
    "nanos": 60469084
  }
}
```
