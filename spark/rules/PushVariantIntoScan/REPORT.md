# PushVariantIntoScan

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 25  **Verification rounds used:** 2
**Scope detail:** a single variant column (two requested fields, one referenced by the filter, one only by the projection) plus one non-variant pass-through column, assuming the rewritten struct-typed scan yields the extracted fields materialized from the same variant values (Spark's reader/VariantMetadata guarantee QED cannot model); the full rule recurses over arbitrary plan shapes, any number of variant columns, and arbitrary filter/projection expressions.


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/PushVariantIntoScan.scala, lines 365-442
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core semantic transformation of PushVariantIntoScan in a narrow but non-degenerate form: before() places the filter (on P(ExtractA(v))) below the projection that materializes extractions, while after() models the "pushed-in" scan as a projection over the original scan, filters on the now-materialized field, and applies an identity projection—proving non-vacuous filter–project commutation under the stated materialization assumption. The SCOPE: PARTIAL tag is honest and specific (one variant column, two requested fields, one pass-through column), symbol sharing is correct (same uninterpreted P, ExtractA, ExtractB, and source scan on both sides), the relational shape (Project∘Filter∘Scan) matches the real rule's structure, and no backend precondition (Parquet format, variant-column existence, config flag) is silently misrepresented—those are acknowledged as unmodelable guarantees that QED correctly does not need to re-verify.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5535083
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35933167
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1058917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 410000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16044458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36036667
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67853250
  }
}
```
