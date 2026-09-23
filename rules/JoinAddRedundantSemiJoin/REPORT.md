# JoinAddRedundantSemiJoin

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache Calcite
**Porter attempts used:** 12  **Verification rounds used:** 1
**Scope detail:** public record JoinAddRedundantSemiJoin() implements RRule {


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinAddRedundantSemiJoinRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding exactly mirrors the source rule's transformation: before = X INNER⋈_C Y, after = (X SEMI⋈_C Y) INNER⋈_C Y, with the same uninterpreted condition C shared in all three joins — which is correct because Calcite's rule reuses `origJoinRel.getCondition()` for both the new semi-join and the outer join — and the correct join kinds (the rule explicitly restricts to INNER, so hard-coding INNER is faithful, not under-generalizing). The two source guards that are absent from the encoding (`isSemiJoinDone` and non-empty `leftKeys`) are bookkeeping/applicability heuristics, not semantic preconditions for correctness: the redundancy identity holds for every condition (e.g. even C referencing only Y or C=true), so QED's proof of the guard-free claim is stronger than, not weaker than, what the rule needs. X and Y are fully uninterpreted single-column relations with an uninterpreted predicate over the joined row — column count plays no role in the transformation's validity, so `// SCOPE: FULL` is honest and the plan shapes are structurally distinct (one join vs. two), making the proof a genuine verification of a real identity rather than a vacuous one. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 15633623
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35745625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 808125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 514667
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 32266459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35873125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 90690500
  }
}
```
