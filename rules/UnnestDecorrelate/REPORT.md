# UnnestDecorrelate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 40  **Verification rounds used:** 2
**Scope detail:** the left subquery is assumed to be the array column plus exactly one extra unused column, and the unnested element relation is single-column


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/UnnestDecorrelateRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's semantic claim: the INNER correlate over a single-row Values feeding a correlated uncollect is abstracted as a bag join of the left with an element relation under the shared uninterpreted membership predicate M, with the outer (and optionally composed inner) projection P structurally applied only to the element column, mirroring the rule's side conditions (INNER only, outer project uses no left columns, single-row values, single correlated array column). The two sides genuinely differ — before joins the full two-column left while after drops the unused column via a projection below the join — so the SMT proof is of a real, non-vacuous bag-semantic column-dropping/projection-commutation identity rather than of identical plans, and all symbols (L, E, M, P) are shared exactly as the rewrite requires, with no spurious uniqueness assumptions. The `SCOPE: PARTIAL` line is honest and specific (left = array column + exactly one unused column; single-column element relation), the narrowing stems from QED's genuine lack of list/uncollect semantics (so a DSL correlate operator wouldn't buy more generality), and the proved special case is non-degenerate and captures the core soundness argument of the original rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7204999
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 62092292
  },
  "smt_timed_out": false,
  "nontrivial_perms": true,
  "translate_duration": {
    "secs": 0,
    "nanos": 876250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 515708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19182916
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 62450041
  },
  "total_duration": {
    "secs": 0,
    "nanos": 97560791
  }
}
```
