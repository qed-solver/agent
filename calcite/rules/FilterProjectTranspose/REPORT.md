# FilterProjectTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 42  **Verification rounds used:** 2
**Scope detail:** single-column uncorrelated project with a single-conjunct filter whose condition references only the projected column


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterProjectTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core mechanism of FilterProjectTranspose — pushing a predicate below a project by substituting the projection expression into the filter condition — for the single-column, single-conjunct case. Before (Filter(F(Proj(s)), Project(Proj, s))) and after (Project(Proj, Filter(F(Proj(s)), s))) are structurally distinct plan shapes that are semantically equivalent under bag semantics for all instantiations of the uninterpreted symbols F and Proj, so the proof is non-vacuous. The SCOPE tag accurately and specifically states the restriction (single column, single conjunct, condition references only that column), which is a genuine DSL limitation since the current `RelRN.project(RexRN)` API only produces single-column projects. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5501875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35609209
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 846541
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 375459
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16421125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35706417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67754459
  }
}
```
