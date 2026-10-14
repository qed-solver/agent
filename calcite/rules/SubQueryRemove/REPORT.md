# SubQueryRemove

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 87  **Verification rounds used:** 6
**Scope detail:** uncorrelated EXISTS subquery directly under a Filter, Calcite's positive logic=TRUE branch (no NOT EXISTS/IN/SCALAR/SOME/UNIQUE or correlated rewrite), single-column outer input


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SubQueryRemoveRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors Calcite's uncorrelated-EXISTS-under-Filter (logic=TRUE) branch: before is `Filter(EXISTS(S), R)` and after is `Project([0], InnerJoin(true, R, GroupBy(true, Project(true, S))))`, which matches the original's `push(S); project(true as i); aggregate(groupKey(0)); join(INNER, true); filter(true→noop); project([0])` sequence. The single-column-R restriction (imposed by `RelRN.scan` producing one column) and the filter-condition-is-purely-EXISTS restriction are both honestly stated in the SCOPE line, and the proof is non-vacuous since the two sides have structurally different shapes (filter-with-subquery vs join-with-aggregate).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6851123
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6952125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 74959
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 487000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 13906750
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7000583
  },
  "total_duration": {
    "secs": 0,
    "nanos": 23822333
  }
}
```
