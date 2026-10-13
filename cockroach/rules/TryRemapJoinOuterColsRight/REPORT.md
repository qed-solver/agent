# TryRemapJoinOuterColsRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 1  **Verification rounds used:** 1
**Scope detail:** the InnerJoinApply case only: both inputs are single-column scans of one shared uninterpreted type, ON is the genuine equality l.c0 = r.c0, and the remapped predicate is an uninterpreted 1-ary h over the right (outer) column, swapped to the equal left column


## Source rule (as given to the porter)

```
(spec text unavailable at publish time)
```

## Independent verifier review

**Verdict:** CONFIRMED

Manual mirror of the already-PROVED TryRemapJoinOuterColsLeft (same PARTIAL scope: InnerJoinApply, single-column scans of one shared uninterpreted type, ON is the genuine correlate equality l.c0 = r.c0). This is the symmetric right-side case: the filter predicate h references the right relation's column pre-remap and the left relation's equal column post-remap (fields swapped relative to the Left variant, which goes left-to-right instead of right-to-left). QED proves it (provable=true) and a negative control that breaks the correlate equality (replaces EQUALS with an uninterpreted g, so nothing forces the two columns equal) correctly fails to prove, confirming the encoding is non-vacuous.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5454124
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6347166
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 53958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 404375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 11010250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6392417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 19877125
  }
}
```
