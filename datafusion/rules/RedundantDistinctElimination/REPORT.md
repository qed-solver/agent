# RedundantDistinctElimination

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** the DISTINCT's input is assumed to be already unique on all of its columns, modelled as a GROUP BY over exactly all of a fixed two-column input's columns (DataFusion's prototypical Dependency::Single-over-all-fields case), so the DISTINCT is a no-op and is simply removed.


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/replace_distinct_aggregate.rs, lines 89-119
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (the set-variant self-intersection of the input) and after() (the bare input) are structurally distinct, and the proven equality is exactly the rule's content — DISTINCT is a no-op when the input is already unique — with the uniqueness precondition present in the encoding as a GROUP BY over all columns (the source comment's prototypical Dependency::Single-over-all-fields case) rather than silently dropped. Modelling Distinct::All as R INTERSECT (set) R is the correct encoding, the symbol reuse across both intersect operands is the intentional self-intersection, and the SCOPE line honestly discloses the remaining narrowings (fixed two-column arity, which is inherent to the DSL's fixed-schema design, and exclusion of the zero-column LIMIT-1 branch, which is unmodelable in QED anyway). ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9926539
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 23906000
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 875917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 616375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 24814958
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 23997708
  },
  "total_duration": {
    "secs": 0,
    "nanos": 64723958
  }
}
```
