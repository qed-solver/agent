# FullJoinRightNullRejectingToRight

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** only the FULL→RIGHT branch is encoded (DataFusion's eliminate_outer case (Full,false,true)), with the null-rejecting filter restricted to an explicit IS_NOT_NULL on the right input's column sitting directly above the join (no intervening projections, whose predicate-inlining is used by the source only for analysis and cannot be modeled as a QED-verifiable property of an uninterpreted predicate).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_outer_join.rs, lines 102-244
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the named branch (DataFusion's `eliminate_outer` case (Full,false,true) → Right): `before()` and `after()` differ only in the join kind (FULL vs. RIGHT), with the ON condition (`on`) and the null-rejecting filter (`IS_NOT_NULL` on the right column, field 1) correctly shared across both sides, matching how the source copies `on`/`filter` and re-wraps the same predicate above the rebuilt join. The two restrictions — a concrete `IS_NOT_NULL` rather than an arbitrary null-rejecting predicate, and no intervening projections — are genuine QED limitations (it cannot reason about an uninterpreted predicate's null-rejection/entailment or track nullness through uninterpreted projection expressions), not missing DSL capabilities, and are honestly labeled in the SCOPE line. The result is non-degenerate and semantically correct (the FULL join's null-extended left-only rows are exactly those killed by `IS_NOT_NULL(r)`, so the bag over FULL-filtered equals the bag over RIGHT-filtered), so the proof reflects a real, meaningful instance of the rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 14046001
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
    "nanos": 967416
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1182416
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 34159375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 50988292
  }
}
```
