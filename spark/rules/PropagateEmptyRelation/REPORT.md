# PropagateEmptyRelation

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 8  **Verification rounds used:** 2
**Scope detail:** the source rule's commonApplyFunc covers joins, unions and unary nodes whose children are empty (or a join with a false literal condition); this encoding pins the INNER-join branch with an empty left side (fixed two-column shape per side).


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PropagateEmptyRelation.scala, lines 219-224
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (an INNER join with an empty left input) and `after()` (an independent empty relation of the join's output schema `l0,l1,r0,r1`) are structurally distinct yet semantically equivalent by INNER-join semantics, so the proof is non-vacuous; the join condition is left as an uninterpreted `pred` and the column types are correctly shared across both sides to assert schema preservation, with no bogus over-constraint. The scope is honestly tagged PARTIAL for a genuine, specific fragment (INNER join, empty left side, fixed two-column arity — the latter being a real DSL arity limitation, and INNER being a sound choice since "any join type / either side empty" is not universally valid, e.g. LEFTANTI/FULLOUTER), making this a faithful, non-degenerate special case of the source rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
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
    "nanos": 1057000
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 203000
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
    "nanos": 1554708
  }
}
```
