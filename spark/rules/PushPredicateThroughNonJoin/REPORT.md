# PushPredicateThroughNonJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** two-column projection over a single-column source with a two-conjunct filter where each conjunct references exactly one projected column; the pushed conjunct is rewritten onto its projection expression and pushed below the projection, the retained conjunct stays above (no projection split)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2567-2961
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models the rule's projection case (the avoid-double-filter-eval branch with no projection split): before is Filter(AND(F(π₀), G(π₁)), Project([P1,P2])) and after is Project([P1,P2]) over Filter(F(P1(x))) with G(π₁) retained on top in alias form — exactly Spark's output, where the pushed conjunct is alias-substituted onto the projection expression (P1, F, G correctly shared across both sides) and the retained conjunct stays above referencing the projection's output column. before() and after() are structurally different (the F-filter moves from above to below the project), so the proof is non-vacuous; the rule's real preconditions (projection determinism per SPARK-13473, no subquery/attribute overlap) are subsumed by QED's deterministic uninterpreted symbols or trivially satisfied by this fragment, and the cheap/expensive distinction is a cost heuristic of the optimizer, not a semantic condition. The PARTIAL scope line accurately and specifically states the restrictions (two-column projection over a single [NOTE: response was truncated at the token limit before finishing — if this cut off mid-code-block, that's why it couldn't be parsed.]

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5522958
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35372166
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1108125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 472709
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16073833
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35471083
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67216417
  }
}
```
