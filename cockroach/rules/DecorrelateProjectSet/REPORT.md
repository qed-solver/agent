# DecorrelateProjectSet

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** one column each for the ProjectSet input and the zip emission; the zip is uncorrelated with the input (the rule's IsZipCorrelated guard, negated) so the emitted-set membership M ranges over the emitted column only, not the input; ProjectSet is abstracted as an INNER join under M and the ConstructNoColsRow-ProjectSet as the M-filtered emission; the input:^(Values) side condition and EmptyJoinPrivate are not modeled (the empty ON is a cross join); these do not affect bag-semantic equivalence.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

DecorrelateProjectSet pulls an input relation outside of a ProjectSet if the
input is not correlated with any of the functions in the ProjectSet. The
input is then cross-joined with a new ProjectSet, which contains the same
functions but has an empty input (a unary VALUES node).

The advantage of this transformation is it means each of the functions in the
ProjectSet only need to be executed once in total, instead of once for each
input row.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `DecorrelateProjectSet`, not the other rules in that file):

```
# DecorrelateProjectSet pulls an input relation outside of a ProjectSet if the
# input is not correlated with any of the functions in the ProjectSet. The
# input is then cross-joined with a new ProjectSet, which contains the same
# functions but has an empty input (a unary VALUES node).
#
# The advantage of this transformation is it means each of the functions in the
# ProjectSet only need to be executed once in total, instead of once for each
# input row.
[DecorrelateProjectSet, Normalize]
(ProjectSet
    $input:^(Values)
    $zip:* & ^(IsZipCorrelated $zip (OutputCols $input))
)
=>
(InnerJoin
    $input
    (ProjectSet (ConstructNoColsRow) $zip)
    []
    (EmptyJoinPrivate)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

It is an honest, non-degenerate partial encoding (unary input and emitted column) of the source rule's core independence premise: because the zip is uncorrelated with the input, the emitted-row predicate M depends only on E, so

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7053750
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33657708
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1062333
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 476667
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19156250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33752917
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68738041
  }
}
```
