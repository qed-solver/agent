# MergeConsecutiveFilters

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 2
**Scope detail:** assumes only a pair of consecutive filters over a shared base relation (local merge); excludes the source's conjunct simplification/reordering and the Limit/Offset-guarded join-pushdown branch.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 799-864
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding matches the source rule's core merge branch exactly — `Filter(Q, Filter(P, R))` → `Filter(P ∧ Q, R)` with the child's conjunct first — using two independent uninterpreted predicates (`inner`, `outer`) over a single shared uninterpreted base relation, which is precisely the symbol structure the real rewrite requires, and `before()` (nested double filter) is genuinely different from `after()` (single filter with `AND`), so the proof is not vacuous. No preconditions are silently dropped: the Limit/Offset guard cannot block this branch (a Filter child has no fetch/skip), predicate deduplication is a semantic no-op under universal quantification, and the one-column scan is the DSL's standard stand-in for an arbitrary base bag that does not weaken the filter-composition claim. The `SCOPE: PARTIAL` tag is honest and specific — it correctly names the excluded conjunct-simplification/cost-reordering steps, recursive application, and the join-pushdown branch that QED cannot express over uninterpreted predicates.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8657667
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34405500
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 866625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 326958
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19497834
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34498750
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69404250
  }
}
```
