# PullOutVariantExtractions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** only the Aggregate case: one grouping-key column and one non-distinct aggregate f over a single variant extraction ve(v) of a scan column, hoisted into a Project directly below the Aggregate (no Sort/Join cases).


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/PullOutVariantExtractions.scala, lines 118-505
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the Aggregate case of the rule: `before` aggregates `f(ve(v))` directly over the scan, while `after` first hoists `ve(v)` into a Project below the Aggregate (dropping the raw variant column, matching the source rule's `passthrough` logic where only group keys + hoisted aliases survive), then aggregates `f` over the projected column. The structural difference is genuine and non-vacuous, the uninterpreted symbols (`ve`, `f`, the scan) are correctly shared, and the SCOPE tag accurately documents that this is a single-group-key, single-aggregate, single-extraction special case with no Sort/Join handling.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
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
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 464625
  }
}
```
