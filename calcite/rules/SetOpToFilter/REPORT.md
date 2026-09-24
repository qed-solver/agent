# SetOpToFilter

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 11  **Verification rounds used:** 1
**Scope detail:** the 2-input UNION DISTINCT instance where both inputs are filters over the same single source with 2-valued predicates (Calcite's rule is general over arity, multiple distinct sources, and non-filter inputs).


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SetOpToFilterRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding exactly captures Calcite's rewrite for the single-source, two-filter case — `Union(DISTINCT, [σ_P1(S), σ_P2(S)])` → all-fields group-by (no agg calls) over `σ_(P1∨P2)(S)` — and is faithful in the details: `union(false, ...)` preserves the rule's `!setOp.all` precondition, and rendering `.distinct()` as a group-by-all-fields Aggregate is precisely what Calcite's `RelBuilder.distinct()` itself builds, so the right side matches the rule's actual output (single branch, no 1-input set op). The proof is non-vacuous (the two sides are structurally different and require genuine filter-over-set-op reasoning with properly uninterpreted P1/P2 over a shared single source, exactly the sharing the rule requires), and the declared `SCOPE: PARTIAL` is honest and specific — 2-input UNION DISTINCT over filters on one shared source is a genuine, non-degenerate special case of the general arity/multi-source/INTERSECT/MINUS rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9599876
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35317792
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 915083
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 485583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23434250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35398375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74560667
  }
}
```
