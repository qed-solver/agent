# LeftJoinNullRejectingToInner

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** the null-rejecting filter is restricted to an explicit IS_NOT_NULL on the right input's column, and only the LeftJoin→InnerJoin variant of eliminate_outer is modeled (other join-type branches and projection inlining are not)


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the Left→Inner branch of `eliminate_outer` with the filter remaining above the join (matching the source's `Filter::try_new(filter.predicate, rebuilt_inner)`), uses uninterpreted symbols for tables and the ON predicate, and correctly references the right-side column via `joinField(1, right)`; the only narrowing is the restriction of the null-rejecting predicate to explicit `IS_NOT_NULL` (rather than any null-rejecting expression) and omission of the projection-inlining layer, both of which are honestly tagged in the SCOPE line and do not make the proof vacuous since LEFT and INNER joins genuinely differ in NULL-extension behavior.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7552542
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
    "nanos": 848958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 670166
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21042250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 36510333
  }
}
```
