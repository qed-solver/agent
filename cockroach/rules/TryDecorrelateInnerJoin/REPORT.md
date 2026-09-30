# TryDecorrelateInnerJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 82  **Verification rounds used:** 5
**Scope detail:** INNER joins only, single-column L/IL/IR, uninterpreted 3-ary on and 2-ary innerOn


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateInnerJoin tries to decorrelate an InnerJoin operator nested
beneath another Join operator by pulling up its join condition to the outer
join. This may be enough to decorrelate the outer join, or it may allow any
outer column references to continue to journey upwards.

TODO(andyk): Consider adding case for outer cols in $left.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateInnerJoin`, not the other rules in that file):

```
# TryDecorrelateInnerJoin tries to decorrelate an InnerJoin operator nested
# beneath another Join operator by pulling up its join condition to the outer
# join. This may be enough to decorrelate the outer join, or it may allow any
# outer column references to continue to journey upwards.
#
# TODO(andyk): Consider adding case for outer cols in $left.
[TryDecorrelateInnerJoin, Normalize]
(InnerJoin | InnerJoinApply | LeftJoin | LeftJoinApply | SemiJoin
        | SemiJoinApply | AntiJoin | AntiJoinApply
    $left:*
    $right:* &
        (HasOuterCols $right) &
        (InnerJoin | InnerJoinApply
            $innerLeft:*
            $innerRight:*
            $innerOn:* &
                ^(FiltersBoundBy
                    $innerOn
                    (OutputCols2 $innerLeft $innerRight)
                )
            $innerPrivate:*
        )
    $on:*
    $private:*
)
=>
((OpName)
    $left
    ((OpName $right) $innerLeft $innerRight [] $innerPrivate)
    (ConcatFilters $on $innerOn)
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core algebraic identity of `TryDecorrelateInnerJoin` for the INNER-join case: [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9281418
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 32054958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 882167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 562000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23755917
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 32201708
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71673083
  }
}
```
