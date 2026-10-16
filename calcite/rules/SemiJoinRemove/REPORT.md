# SemiJoinRemove

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 2
**Scope detail:** the matched semi-join must be the left input of an inner join that shares both the same join condition and the same right relation, i.e. (X SEMI⋈_C Y) INNER⋈_C Y ⇒ X INNER⋈_C Y; this local redundancy condition stands in for the source rule's global "all SemiJoins in the plan are advisory" side condition, which has no form as a local pattern constraint.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully mirrors the source rule's transformation — replacing a SEMI join with its left input (`getInput(0)`), with correct join kinds and uninterpreted, shared condition/right symbols rather than hard-coded predicates — and `before()`/`after()` are structurally distinct, so the proof is non-vacuous. The PARTIAL scope is honest and forced by a genuine limitation: the source rule's soundness rests on the global "all SemiJoins are advisory" plan property, which has no form as a local bag-equivalence (the unconditioned claim L SEMI⋈ R ≡ L is actually false, since a row of L with no match in R is dropped), so the locally-guaranteed redundancy instance the porter encoded is the only QED-provable statement and is a genuine, non-degenerate special case.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6834833
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 7327292
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 53417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 461333
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 13952250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7377625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 24322292
  }
}
```
