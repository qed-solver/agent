# ValuesReduce

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 42  **Verification rounds used:** 2
**Scope detail:** the project over the concrete non-empty Values is an input-reference-only column


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ValuesReduceRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a genuine, non-vacuous trace of the rule's PROJECT config — Project([c,a]) over the 2-row Values (1,2,3),(4,5,6) folds to the concrete Values (3,1),(6,4) with the tuples correctly recomputed, the Project node eliminated, and non-empty Values precondition satisfied — so before() and after() are structurally different and the proof is not vacuous. The heavy concreteness (fixed tuples, fixed projection, no filter) is intrinsic rather than a symbol-sharing or under-generalization error: ValuesReduce is a constant-folding rule whose inputs and outputs are literal data that QED cannot stand in for with uninterpreted symbols, so no uninterpreted/general form is expressible, and the absence of the filter half is visible in before() and covered by the honest, specific PARTIAL scope tag (input-reference-only project). A stronger instance (e.g. the doc's `a - b` / `a + b > 4` example) would require literal/comparison builders the DSL lacks, but the proven instance is a real, correctly-computed special case rather than a degenerate identity. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5861417
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 40349624
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 867666
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 373458
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 17267542
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 40423209
  },
  "total_duration": {
    "secs": 0,
    "nanos": 73364667
  }
}
```
