# RewriteComparisonViaUdfPreimage

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 43  **Verification rounds used:** 4
**Scope detail:** only the `=` comparison is encoded (the other five operators have the identical shape); the UDF is modeled as an fx column plus an explicit side-condition P stating the step-function preimage contract, with the distinct-from/NULL and in-list variants excluded


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/udf_preimage.rs, lines 1-69
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core semantic content of DataFusion's `rewrite_with_preimage` for the `=` case: under the step-function contract (which correctly encodes that the UDF outputs `val` iff its input falls in `[lower, upper)`), filtering on `fx = val` is equivalent to filtering on `e >= lower AND e < upper`. The before/after predicates are structurally distinct (one references the UDF output column, the other references the input column with range bounds), so the proof is non-vacuous. The SCOPE line honestly states that only `=` is encoded and that NULL-handling variants are excluded, which is a genuine and non-degenerate special case (the `=` case being the most semantically rich, producing a conjunction rather than a single comparison). The non-nullable `Int` type correctly handles the "x is not NULL" precondition from the source rule, and modeling the preimage contract as a shared filter side-condition is the standard and appropriate technique in this DSL for expressing a UDF's registered semantic property.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7749959
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 38226250
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 836042
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 469375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18614958
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 38349000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72795167
  }
}
```
