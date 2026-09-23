# FilterReduceExpressions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** the filter condition is the constant FALSE literal (the branch where constant reduction of the condition yields a constant false, so the filter is replaced by an empty relation with the input's row type via createEmptyRelOrEquivalent)


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ReduceExpressionsRule.java

Note: ReduceExpressionsRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `FilterReduceExpressionsRule` variant (not CalcReduceExpressionsRule, JoinReduceExpressionsRule, ProjectReduceExpressionsRule, WindowReduceExpressionsRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding precisely mirrors the source rule's false-literal branch of `onMatch` — `newConditionExp instanceof RexLiteral` (false) → `createEmptyRelOrEquivalent`, i.e. `builder.push(filter.getInput()).empty()`, which is exactly `source.filter(RexRN.falseLiteral())` ⟹ `source.empty()` (zero-row `Values` of the input's row type) — and the two sides are structurally different plans (Filter node vs. empty Values), so the provable equivalence is a genuine, non-vacuous statement of the real optimization (input scan eliminated). There are no silently dropped preconditions: this branch fires for any input regardless of keys/NOT NULL (those only gate the separate `reduceNotNullableFilter` path), and the single input relation is correctly a single scan symbol. The restriction to a condition that is *already* the constant FALSE literal is a genuine QED limitation rather than a missing DSL feature — the rule's general power is executor-based constant reduction of arbitrary subtrees (e.g. `1+1=2`), and QED cannot reason about the algebra of uninterpreted operator symbols, so the top-level literal case is the meaningful expressible special case — and the one-line `// SCOPE: PARTIAL` tag discloses exactly this, specifically and honestly.

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
    "nanos": 394459
  }
}
```
