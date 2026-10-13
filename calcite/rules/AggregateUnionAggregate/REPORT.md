# AggregateUnionAggregate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** the two union inputs are one-column relations of the same type (arbitrary-arity versions are not modeled) and the inner dedup is fixed to the second union input; both aggregates are group-by-all-columns with no aggregate calls, matching Calcite's isSimple / no-agg-call requirement.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateUnionAggregateRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful and non-vacuous: `before()` truly differs from `after()` (an extra group-by-all, no-agg-call dedup beneath the second UNION ALL arm), and the proved identity dedup(A ⊎ dedup(L)) ≡ dedup(A ⊎ L) is exactly the algebraic content of Calcite's rule — UNION ALL (`all=true` as the source requires), top aggregate `isSimple` (group by all columns, no agg calls), bottom aggregate with no agg calls, and L/A are independent scans sharing only the row type the union itself mandates, so there is no triviality, wrong operator, sharing error, or silently dropped precondition. It is an honest, specific PARTIAL special case (single-column arms, inner dedup fixed to the second arm, and the bottom aggregate group-by-all rather than Calcite's allowed subset-group-set with an inserted Project), but the restriction is genuine rather than degenerate: the rule still removes a real aggregation, and the dropped generality (multi-column rows, which the current one-column `RelRN.scan` cannot express, and first-arm placement, which follows by UNION ALL symmetry) is what it is.

(This proof relies on an accepted RuleScript DSL extension made during this session — see the extended file(s) for what changed.)

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11601751
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25499042
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 933542
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 557334
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 27496375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25613041
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68903375
  }
}
```
