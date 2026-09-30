# SimplifyJoinFilters

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** only removes a True ON item, INNER join only


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

SimplifyJoinFilters works like SimplifySelectFilters, except that it operates
on Join filters rather than Select filters.

Extracted from `join.opt` (which defines multiple rules — implement specifically `SimplifyJoinFilters`, not the other rules in that file):

```
# SimplifyJoinFilters works like SimplifySelectFilters, except that it operates
# on Join filters rather than Select filters.
[SimplifyJoinFilters, Normalize, HighPriority]
(Join
    $left:*
    $right:*
    $on:[
            ...
            $item:(FiltersItem
                    (And | True | False | Null | Or | Is)
                ) &
                ^(IsUnsimplifiableOr $item) &
                ^(IsUnsimplifiableIs $item) &
                ^(IsContradiction $item)
            ...
        ] &
        ^(IsFilterFalse $on)
    $private:*
)
=>
((OpName) $left $right (SimplifyFilters $on) $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (`ON rest AND True`) and after() (`ON rest`) are structurally distinct, share exactly one uninterpreted `rest` predicate over the join fields with independent L/R scans, and use the correct conjunction/inner-join shape, so the proof is a genuine, non-vacuous check rather than a coincidental over-constraint; the source rule's guards (IsUnsimplifiableOr/Is, IsContradiction, IsFilterFalse) are anti-firing conditions, and their absence only strengthens the proved claim, since the equivalence holds for every instantiation of `rest`. The restriction to True-item removal on INNER joins is honestly declared in the SCOPE line, is specific and genuine, and is largely forced by the single-file contract — the full SimplifyFilters is a backend simplification function that no single structural rewrite can express, and one before/after pair can fix only one join kind — while the result remains a non-degenerate, verifiable instance of the real rule's behavior (True is among the item kinds the source rule explicitly matches). ```

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
    "nanos": 342250
  }
}
```
