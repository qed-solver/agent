# MergeSelects

**Status:** PROVED  **Scope:** FULL
**Source backend:** CockroachDB
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** public record MergeSelects() implements RRule {


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

MergeSelects combines two nested Select operators into a single Select that
ANDs the filter conditions of the two Selects.

Extracted from `select.opt` (which defines multiple rules — implement specifically `MergeSelects`, not the other rules in that file):

```
# MergeSelects combines two nested Select operators into a single Select that
# ANDs the filter conditions of the two Selects.
[MergeSelects, Normalize]
(Select (Select $input:* $innerFilters:*) $filters:*)
=>
(Select $input (ConcatFilters $innerFilters $filters))
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures MergeSelects: `before()` is two nested filters (inner then outer) and `after()` is a single filter on the AND of the two uninterpreted predicates, which is exactly the source rule `(Select (Select $input $innerFilters) $filters) => (Select $input (ConcatFilters $innerFilters $filters))` since a Select's filter list is semantically the conjunction of its items. The two predicates are independent uninterpreted symbols (no spurious sharing), the input is a single uninterpreted scan (the standard stand-in for "any relation" in this DSL, with the row domain abstracted away so column count is irrelevant to the logical identity), and the source rule has no side conditions, so nothing is missing. SCOPE: FULL is honest — the proof covers arbitrary predicates over arbitrary relations, which subsumes the real rule's "any filter lists" generality. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5581333
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34809916
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 899208
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 328375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16376250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34899125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 66762250
  }
}
```
