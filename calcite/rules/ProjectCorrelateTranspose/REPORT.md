# ProjectCorrelateTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 62  **Verification rounds used:** 3
**Scope detail:** uncorrelated INNER correlate modeled as a true-condition inner (cross) join, projections restricted to bare field references (fixed 2-column-per-side shape with one column pruned per side)


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectCorrelateTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-vacuous special case: before() projects above a 4-column cross product while after() prunes each input to one column first, so the plans are structurally different and the SMT proof did real work, and an uncorrelated INNER Correlate is semantically exactly a true-condition INNER join, so the shape corresponds to an instance where Calcite's rule actually fires (one column pruned per side defeats locateAllRefs(), and the trailing identity top-project mirrors the rule's always-emitted newProject). The PARTIAL tag is specific and honest: the uncorrelated/bare-reference/INNER/2-column-per-side restrictions are declared, the essential generality gap (re-indexing of correlated RexFieldAccess and other correlate join types) is a genuine DSL limitation since no Correlate builder exists, and the remaining result is still a real, useful column-pruning-below-product rule rather than a degenerate one. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7254501
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 40553875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1053917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 521834
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19275792
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 40647333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 76322833
  }
}
```
