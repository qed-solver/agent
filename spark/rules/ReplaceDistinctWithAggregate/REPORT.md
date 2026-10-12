# ReplaceDistinctWithAggregate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the input has exactly two columns (the DSL's fixed scan arity), which are all the columns DISTINCT deduplicates on, and the Distinct node is modeled as a set-variant self-intersection, the only deduplication construct QED models.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 3333-3338
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful and non-vacuous: `before()` (set-variant self-intersection, i.e. set(R)) and `after()` (GROUP BY on all columns with zero aggregate calls) are structurally distinct operator shapes, and the single `Input` scan symbol is reused exactly where the source rule requires it (the same relation on both arms of the intersect, an arbitrary non-unique bag), so the proven fact set(R) = group-by-all-columns(R) is precisely the semantic identity underlying Spark's `Distinct(child) => Aggregate(child.output, child.output, child)`. Column types remain uninterpreted (`Col0_Type`, `Col1_Type`), so nothing concrete is baked in, and the only restrictions — fixed 2-column arity and Distinct being modeled as the only dedup construct QED models — are genuine DSL/QED limitations (no variable-arity scans, no bag-variant INTERSECT) that are stated specifically in the SCOPE line, leaving a useful, non-degenerate special case rather than a trivial identity. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7832209
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25594542
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1497375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 687042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 20179292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25685916
  },
  "total_duration": {
    "secs": 0,
    "nanos": 61505625
  }
}
```
