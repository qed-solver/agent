# CleanupDynamicPruningFilters

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 25  **Verification rounds used:** 3
**Scope detail:** the dynamic pruning filter is modeled as an uninterpreted probe-side predicate that is also conjunctively enforced by the enclosing inner join's condition, rather than as an IN(subquery) implied by key equality


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/dynamicpruning/CleanupDynamicPruningFilters.scala, lines 35-94
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous and faithful to the rule's core: before() differs from after() only by the probe-side filter, and the rewrite is valid precisely because the uninterpreted DPP predicate `dpp` is enforced as a conjunct of the inner join's condition — the deliberate sharing of `dpp` between filter and join condition correctly models the dependency (join condition subsumes the DPP filter) rather than an accidental over-constraint, with no hard-coded predicates or wrong operator shapes. The restriction to this conjunctive/inner-join form is genuine (QED cannot reason about implication from an IN-subquery predicate to a join condition, as the real DPP filter would be), is accurately disclosed in the SCOPE: PARTIAL line, and still yields a useful, universally quantified soundness lemma over arbitrary L, R, and predicates. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7425166
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36402125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1075583
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 450583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19862000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36505083
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72360250
  }
}
```
