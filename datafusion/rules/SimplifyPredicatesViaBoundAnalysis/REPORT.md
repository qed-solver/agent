# SimplifyPredicatesViaBoundAnalysis

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 52  **Verification rounds used:** 3
**Scope detail:** only the same-direction bound-reduction branch: a conjunction of two lower bounds on one column keeps the larger (e.g. x > 5 AND x > 6 to x > 6), omitting the empty-range-to-false, equality subsumption and !=-drop branches


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/simplify_predicates.rs, lines 1-389
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (filter x>5 AND x>6) and after() (filter x>6) are structurally distinct, and their equivalence is non-vacuous — it requires the ordering fact 5<6, which QED genuinely discharged — so this is a real instance of the source rule's same-direction bound-reduction branch, matching exactly the x > 5 AND x > 6 → x > 6 example documented in the Rust source. Concrete literals are unavoidable here: making the bounds or comparison uninterpreted symbols would not generalize the rule but falsify it, since the rewrite's soundness rests on the actual ordering of the values. The SCOPE line honestly flags PARTIAL and names the omitted branches (empty-range-to-false, equality subsumption, !=-drop), the relational shape (Filter over a conjunction on one column, GREATER_THAN) matches the source, and no preconditions (keys, nullness) are missing — so the proof is of a genuine, non-degenerate special case, not of a vacuous or coincidentally over-constrained claim. ```

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
    "nanos": 330375
  }
}
```
