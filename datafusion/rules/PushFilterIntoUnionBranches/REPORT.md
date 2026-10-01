# PushFilterIntoUnionBranches

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** exactly two one-column branches of the same type T (DataFusion's rule handles any number of possibly multi-column branches); the predicate is pushed unchanged (identity column renaming) into both branches.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 988-1011
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous (the filter moves from above the UNION ALL down into each branch, which is exactly DataFusion's rewrite) and the symbols are handled correctly: A and B are independent tables, the shared type T reflects the union's identical-branch-schema requirement rather than an extra assumption, `all=true` matches `LogicalPlan::Union` (UNION ALL), and reusing the uninterpreted predicate P across all three filter occurrences is precisely the right model of the source's per-branch column rename (positionally identity in this shape). It is narrower than the source — exactly two single-column branches instead of any arity and column count — but arity and column count are fixed-shape parameters that no single RuleScript pattern can quantify over, the SCOPE tag discloses the restriction specifically and accurately, and the two-branch case is the minimal non-degenerate instance, so the provable result faithfully covers a genuine PARTIAL instance of the real rule.

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
    "nanos": 45375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 364583
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
    "nanos": 522500
  }
}
```
