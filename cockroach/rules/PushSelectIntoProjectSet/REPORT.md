# PushSelectIntoProjectSet

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** the ProjectSet input has one column, the zip emits one column of rows, there is exactly one bound filter conjunct (on the input column, pushed below the ProjectSet) and one unbound conjunct (on the synthesized column, kept above); the zip's row-emission membership predicate is uninterpreted over the input column and the emitted row, and the bound conjunct does not reference any column the membership predicate depends on (IsBoundBy guard).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

PushSelectIntoProjectSet pushes filters into a ProjectSet. In particular,
the filters that are bound to the input columns of the ProjectSet are
pushed down into it, in hopes of being pushed down further into joins
and scans underneath the ProjectSet.

Extracted from `select.opt` (which defines multiple rules — implement specifically `PushSelectIntoProjectSet`, not the other rules in that file):

```
# PushSelectIntoProjectSet pushes filters into a ProjectSet. In particular,
# the filters that are bound to the input columns of the ProjectSet are
# pushed down into it, in hopes of being pushed down further into joins
# and scans underneath the ProjectSet.
[PushSelectIntoProjectSet, Normalize]
(Select
    (ProjectSet $input:* $zip:*)
    $filters:[
        ...
        $item:* &
            (IsBoundBy $item $inputCols:(OutputCols $input))
        ...
    ]
)
=>
(Select
    (ProjectSet
        (Select
            $input
            (ExtractBoundConditions $filters $inputCols)
        )
        $zip
    )
    (ExtractUnboundConditions $filters $inputCols)
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The ProjectSet is soundly abstracted as an INNER join of its input L with an emitted bag E under an uninterpreted pairing predicate M — INNER correctly models the row-dropping behavior when the lateral set yields zero rows, the universal quantification over M covers both zipped and unzipped modes, and the bound conjunct P references only the passed-through input column (structurally enforcing the IsBoundBy guard) while the unbound conjunct Q references only the synthesized column, with the same symbols (M, P, Q, L, E) shared correctly between both sides. before() and after() are genuinely different plans (P filters above vs. below the join), so the proof is non-vacuous and encodes exactly the source rule's transformation (Select over ProjectSet ⟹ Select over ProjectSet-over-Select, with filters split bound/unbound). The honestly tagged PARTIAL restrictions — one input column, one emitted column, exactly one bound conjunct and one unbound conjunct, with the unbound conjunct restricted to the synthesized column — are specific rather than vague, remain a non-degenerate instance of the rule, and the single-conjunct form effectively subsumes arbitrary conjunctions since P and Q range over all uninterpreted predicates.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 4941250
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6405583
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 66958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 426709
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 10542500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6450750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 19624875
  }
}
```
