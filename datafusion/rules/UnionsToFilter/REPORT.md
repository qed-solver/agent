# UnionsToFilter

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** exactly two branches over the same one-column source, each with a filter predicate, merged into one OR-filtered branch; DataFusion's rule also handles any arity, multi-column sources, projection/subquery-alias wrappers, and unfiltered (constant-true) branches.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/unions_to_filter.rs, lines 1-381
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous (UNION ALL of two filtered branches vs. a single OR-filtered branch) and semantically faithful to DataFusion's plan shape: `LogicalPlan::Union` is a bag union with `Distinct::All` on top, correctly encoded as `union(true, ...)` wrapped in an all-columns group-by with no aggregate calls (the standard dedup encoding; if QED's group-by did not deduplicate, the equivalence would be refuted by any row satisfying both predicates, so the PROVABLE result confirms dedup semantics is in effect). Symbol sharing is correct: both branches reuse the same scan `S` exactly as the rule's `GroupKey` identity requirement demands, while `P1`/`P2` are independent uninterpreted predicates, and DataFusion's volatility/`is_repeatable` preconditions are vacuous under QED's deterministic uninterpreted-symbol model, so no precondition is silently missing. The `SCOPE: PARTIAL` line is present, one sentence, and precisely names the genuine restrictions (exactly two branches, one-column source, both branches filtered, no projection/alias wrappers), so the under-generalization is honestly declared and the proved lemma — DISTINCT(F_{P1}(S) ⊎ F_{P2}(S)) = DISTINCT(F_{P1∨P2}(S)) — is still a real, non-degenerate core of the rule. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7015583
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36712917
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 65584
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 488375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16509916
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36748042
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67525541
  }
}
```
