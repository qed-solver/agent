# TryDecorrelateProjectSet

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3
**Scope detail:** one column each for left, ProjectSet input, and zip emission; ProjectSet abstracted as an INNER join under uninterpreted membership M and InnerJoinApply as a plain INNER join (correlation/row-emission unmodeled); $private join attributes dropped.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/decorrelate.opt

TryDecorrelateProjectSet "pushes down" an InnerJoinApply operator into a
ProjectSet operator, in hopes of eliminating any correlation between the
ProjectSet operator and the InnerJoinApply operator. Eventually, the
hope is to trigger the DecorrelateJoin pattern to turn JoinApply operators
into non-apply Join operators.

Extracted from `decorrelate.opt` (which defines multiple rules — implement specifically `TryDecorrelateProjectSet`, not the other rules in that file):

```
# TryDecorrelateProjectSet "pushes down" an InnerJoinApply operator into a
# ProjectSet operator, in hopes of eliminating any correlation between the
# ProjectSet operator and the InnerJoinApply operator. Eventually, the
# hope is to trigger the DecorrelateJoin pattern to turn JoinApply operators
# into non-apply Join operators.
[TryDecorrelateProjectSet, Normalize]
(InnerJoinApply
    $left:*
    (ProjectSet $input:* $zip:*)
    $on:*
    $private:*
)
=>
(Select
    (ProjectSet (InnerJoinApply $left $input [] $private) $zip)
    $on
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core structural rewrite: before is `L ⋈_on (I ⋈_M E)` and after is `Filter_on ((L×I) ⋈_M E)`, which are genuinely different plan shapes whose bag-semantic equivalence (valid because M references only I and E columns, not L) is exactly what the original Optgen rule rewrites. The single-column-per-relation choice is a cosmetic restriction that doesn't affect the structural argument (uninterpreted predicates' arity is irrelevant to the join-rearrangement identity), the ProjectSet-as-join abstraction correctly isolates the row-emission relation from L, and the SCOPE tag honestly documents all simplifications.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8284125
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34027958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 826250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 598584
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21375125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34137708
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71085916
  }
}
```
