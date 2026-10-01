# PushFilterIntoWindow

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** the window is modeled as a partition-determined uninterpreted aggregate joined back to its input on a single partition column (no frame/ordering semantics, single window function, two-column input), the filter is a conjunction of exactly one conjunct referencing only the partition column (the conjuncts DataFusion pushes below the window, its potential-partition-keys precondition encoded by construction) and one conjunct over the window output row (kept above).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 1068-1139
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's essence: the window is a join-back of the input to a partition-keyed uninterpreted aggregate (w), the pushed conjunct P is an uninterpreted predicate over *only* the partition column k (exactly DataFusion's "all referenced cols ∈ intersection of partition keys" precondition, making it constant per partition), and the kept conjunct Q is an uninterpreted predicate over the full window row; before() = σ_{P∧Q}(R ⋈ Agg(R)) and after() = σ_Q(σ_P R ⋈ Agg(σ_P R)) are genuinely different plans whose equivalence hinges precisely on P eliminating whole partitions (so the aggregate's input bag, and thus w, is unchanged for surviving rows), so the QED proof is non-trivial and meaningful rather than vacuous. The narrowing to a single window function, single partition column, two-column input, and non-null key is a legitimate special case, is honestly declared in the SCOPE line, and leaves a non-degenerate rewrite; symbol sharing (same P, Q, and w on both sides, equi-join on k) is correct.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12502666
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 48734499
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 986375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1134708
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 28953125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 49122333
  },
  "total_duration": {
    "secs": 0,
    "nanos": 94608208
  }
}
```
