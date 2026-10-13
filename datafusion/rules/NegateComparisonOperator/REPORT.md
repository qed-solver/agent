# NegateComparisonOperator

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 42  **Verification rounds used:** 3
**Scope detail:** only the Gt→Le pair of negate_clause's operator-flip case is encoded (NOT(x > y) ⟺ x <= y); the full rule covers additional operator pairs (Eq→Ne, Lt→Ge, Le→Gt, GtEq→Lt) and its non-comparison branches (De Morgan, double negation, null checks, in-list, between).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/utils.rs, lines 314-317
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-degenerate slice: before() = Filter(NOT(x > y)) and after() = Filter(x <= y) over the cross product of two arbitrary same-type scans, so x and y remain universally quantified values, and the concrete GREATER_THAN/LESS_THAN_OR_EQUAL operators are exactly what must be concrete — QED cannot relate independent uninterpreted predicates, so an uninterpreted-operand version of this rule would be unprovable by design rather than by over-restriction. The carrier shape (INNER join with a TRUE condition, i.e. a pure Cartesian product) imposes no structural assumption on the source plan, symbol sharing is correct (two distinct fields of the same orderable type, matching a comparable operand pair), and the inequality flip NOT(x > y) ⟺ x <= y holds even under SQL three-valued logic, so no NOT NULL or total-order precondition is silently missing. The PARTIAL scope line is accurate and specific — only the Gt→Le pair of op.negate() is encoded, with the other comparison pairs and De Morgan/double-negation/in-list/between branches explicitly excluded — and the two sides are structurally and semantically distinct, so the proof verifies the genuine rewrite rather than a vacuous identity.

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
    "nanos": 338875
  }
}
```
