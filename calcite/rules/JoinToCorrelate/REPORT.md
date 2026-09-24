# JoinToCorrelate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 1  **Verification rounds used:** 1
**Scope detail:** INNER join only; condition is a single uninterpreted binary predicate over one left column and one right column


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToCorrelateRule.java
```

## Independent verifier review

**Verdict:** MANUAL

Hand-implemented by the harness operator after 5 automated rounds all crashed on LLM context-overflow before ever calling try_rule (see round_05_summary.md) — the verifier's own analysis across those rounds had already worked out that JoinToCorrelate needed a new RelRN.Correlate DSL primitive (RelRN/RexRN had no correlated-field-access builder) and a concrete encoding plan; this attempt follows that plan directly. before() is left INNER-join right on one uninterpreted binary predicate `cond` over (L.col, R.col); after() is a new Correlate RelRN whose semantics() builds a real Calcite LogicalCorrelate: a fresh CorrelationId, a RexCorrelVariable over left's row type, and a LogicalFilter on right whose condition applies the SAME `cond` operator to (correlated left field, right's own field) — verified via JSONSerializer.java that Calcite's own JSON encoding already numbers correlated field access identically to a plain join's combined-row indexing, so `cond` is seen by QED as the exact same uninterpreted symbol on both sides. QED proved it on the very first try_rule call (provable=true, no compile errors, no retries). SCOPE: PARTIAL is honest — INNER join only, single uninterpreted binary predicate, no additional filter conjuncts; the source rule's RIGHT-join branch (which needs an input swap + condition remap) is not covered.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6698210
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 32587375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 839125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 453709
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18598459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 32854834
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67055084
  }
}
```
