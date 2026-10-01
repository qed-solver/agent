# EliminateAggregateDistinct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the aggregated column is the input relation's unique key and is also the group-by key, so each group holds at most one row and dropping DISTINCT is a per-group no-op; the full rule (Insensitive aggregates such as min/max/bool_and are idempotent, so DISTINCT never changes their value over arbitrary inputs) is unprovable in QED, which models every aggregate as an uninterpreted function knowing only bag-equality of its input.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_aggregate_distinct.rs, lines 1-163
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source rule's transformation shape — an Aggregate node with identical group-by whose single aggregate call has the DISTINCT modifier dropped — and is non-vacuous: before() and after() are structurally different (distinct=true vs false on the same uninterpreted aggregate "f" over the same operand symbol, unified by name), and the proof genuinely depends on the declared unique-key constraint, since over an unconstrained table the claim would be false for an uninterpreted f. The narrowing to a one-column table whose group key is the table's unique key (making every group a ≤1-row bag over which deduplication is the identity) is a genuine QED limitation rather than a DSL gap — QED's uninterpreted-aggregate model cannot exploit min/max/bool_and idempotence, which is the full rule's actual justification, and no DSL extension could teach the frozen prover that algebra — and the SCOPE: PARTIAL line states this assumption specifically and honestly, with no symbol-sharing or operator-shape errors.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8032375
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 37864542
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1266666
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 542500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 20550459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 38012500
  },
  "total_duration": {
    "secs": 0,
    "nanos": 73681958
  }
}
```
