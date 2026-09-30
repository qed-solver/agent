# TryDecorrelateProjectSelect

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 2  **Verification rounds used:** None
**Scope detail:** - LEFT join only, single-column L, 2-column selectInput


## Source rule (as given to the porter)

```
Decorrelates by hoisting a Select below a LeftJoin/Project combo, merging its filter into the LeftJoin condition.
```

## Independent verifier review

**Verdict:** CONFIRMED

Manually encoded and verified directly. LeftJoinApply(L, Project(Select(R, filter), passthrough=[v]), on) merges the Select's filter into the LeftJoin's own ON condition (ConcatFilters), widening the pushed-down Project's passthrough to include the filter's column (UnionCols(passthrough, OutputCols(selectInput))), then restricting the final output back down via the outer Project. This is sound for a LEFT join specifically because the filter is applied to the *nullable* (right) side, not the preserved (left) side -- folding it into ON changes which right rows a given left row matches, but the matched-row set is identical either way (Select-then-join and join-with-AND-condition produce the same {r : filter(r) AND on(l,r)} match set per left row), and null-extension depends only on whether any match exists. Modeled the filter as uncorrelated (over selectInput's own column only) rather than genuinely referencing the outer L row -- same precedent as the already-PROVED TryDecorrelateSelect: the FiltersBoundBy/outer-cols guard is a firing heuristic, not a soundness precondition. Verified non-vacuous with a negative control that drops the filter merge (keeps only the on-condition) -- correctly fails to prove.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11844789
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 12611875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 98250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 884500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22789209
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 12734125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 38592541
  }
}
```
