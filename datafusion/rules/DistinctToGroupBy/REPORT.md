# DistinctToGroupBy

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** the input has exactly two columns, which are all the columns the DISTINCT deduplicates on (fixed DSL scan arity), and only the main branch is encoded (Distinct over all columns ⟹ GROUP BY over all columns with no aggregate calls), while the zero-column LIMIT-1 and the unique-input drop-the-Distinct corner branches are not expressible here.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/replace_distinct_aggregate.rs, lines 89-127
```

## Independent verifier review

**Verdict:** CONFIRMED

before() models DataFusion's Distinct(All(input)) as a set-variant self-intersection (all=false, the only intersect variant QED models, yielding each input row exactly once = bag-distinct) and after() is exactly the source's main-branch target, Aggregate::try_new(input, group_expr, vec![]) with group keys = all input columns in order and zero aggregate calls; the two sides are structurally distinct, share the single `input` symbol correctly, and the equivalence is a genuine non-vacuous semantic fact. The disclosed exclusions are genuine: the zero-column LIMIT-1 branch has no bag-semantic counterpart in QED, and the unique-input short-circuit is a separate transform that a single before/after pair can't conditionally express (the main-branch rewrite remains valid even on unique inputs). The fixed two-column, non-nullable scan follows the project's house convention (the reference FilterMerge example is likewise a fixed single-column scan tagged FULL), is honestly tagged PARTIAL with the main-branch restriction spelled out, and the type names remain uninterpreted, so the proof covers the full family of that arity — a faithful, non-degenerate encoding of the rule's core rewrite. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7721875
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 24829875
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 848250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 496459
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 20362292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 24918084
  },
  "total_duration": {
    "secs": 0,
    "nanos": 60773250
  }
}
```
