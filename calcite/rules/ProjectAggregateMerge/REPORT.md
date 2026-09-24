# ProjectAggregateMerge

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 36  **Verification rounds used:** 2
**Scope detail:** encodes only the unused-aggregate-call-removal half (single group key, project expressions as plain field references that keep every group key and drop one of three aggregate calls); the COALESCE(SUM(x), 0) to SUM0(x) conversion is not modeled because QED treats the two aggregate calls as distinct uninterpreted group-bag functions with no null-handling identity between them.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectAggregateMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and correctly structured: before() computes an unused third aggregate per group and projects around it (fields 0,1,3 of 4), while after() omits that call and identity-projects the reduced aggregate (fields 0,1,2 of 3) — genuinely different plans over properly introduced and shared uninterpreted symbols (distinct ops f1/f2/f3, the same group-key op g and table S on both sides), matching the exact Project-over-Aggregate shape and column permutation Calcite's rule produces for this input family, with no precondition the removal half requires. The shape restrictions (single group key, plain field-reference projections, one of three calls) are a genuine, specific, non-degenerate sub-case of the real rule rather than a symbol-sharing artifact, and the excluded COALESCE(SUM,0)→SUM0 half rests on aggregate-function algebra QED fundamentally cannot model (it treats distinct aggregate calls as unrelated uninterpreted group-bag functions), so both limitations are real and are accurately disclosed by the concrete SCOPE: PARTIAL line — making the universal proof meaningful rather than vacuous or over-claimed. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 18718082
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 39100833
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1179834
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1336917
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 41052042
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 39276292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 97910084
  }
}
```
