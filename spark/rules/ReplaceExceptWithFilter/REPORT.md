# ReplaceExceptWithFilter

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 53  **Verification rounds used:** 4
**Scope detail:** single-column base (DSL's fixed scan arity), so the rule's multi-column sameResult check and attribute-by-name remapping are vacuous.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/ReplaceExceptWithFilter.scala, lines 44-118
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the rule's core: `Except(left,right,false)` → `minus(false, …)` on a shared base (matching the rule's "same base" eligibility), the right-side filter predicate stays uninterpreted, and `Not(COALESCE(c,false))` is correctly expanded to `Not(pR) OR IS_NULL(pR)` under 3-valued logic, with the `Distinct` wrapper modeled as `f.intersect(false,f)`; before() and after() are structurally different yet semantically equivalent (set of values where P_L holds and P_R is false/null), so the proof is non-vacuous, and the single-column restriction is an honestly and specifically labeled PARTIAL scope that is still a non-degenerate, useful rewrite.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8279251
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 19556542
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 63750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 502209
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16082917
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 19602500
  },
  "total_duration": {
    "secs": 0,
    "nanos": 38897417
  }
}
```
