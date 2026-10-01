# EmptyUnionBranchElimination

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** assumes the union has exactly one zero-row empty branch among three branches with two non-empty branches remaining (the >=2-remain case; the collapse-to-single-branch and all-empty cases are not modeled).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 190-231
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (3-branch UNION ALL containing a zero-row, schema-matching branch) and after() (2-branch UNION ALL) are structurally distinct, so the proof is non-vacuous and establishes exactly the algebraic fact at the core of the DataFusion rule — eliminating a zero-row branch from a bag union is a no-op — with the remaining branches as independent uninterpreted scans sharing the union's row type and the correct all=true (UNION ALL) flag matching DataFusion's LogicalPlan::Union. The PARTIAL scope tag is honest and specific: it models only the ≥2-branches-remain case, while the collapse-to-single-branch and collapse-to-empty sub-cases (and arbitrary arity in general) cannot be captured by any fixed-arity pattern, so this is a legitimate, non-degenerate special case rather than a vacuous or coincidental one.

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
    "nanos": 35167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 281375
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
    "nanos": 416625
  }
}
```
