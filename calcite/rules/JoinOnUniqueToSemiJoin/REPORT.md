# JoinOnUniqueToSemiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 81  **Verification rounds used:** 5
**Scope detail:** 1-column-per-side equi-join on R's unique key column, INNER variant only, with a parent project that uses only the left column


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinOnUniqueToSemiJoinRule` variant (not AggregateToSemiJoinRule, JoinToSemiJoinRule, ProjectToSemiJoinRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding mirrors the source rule's INNER case exactly — Project(Left)∘(L ⋈_{l.c=r.c} R) ⟹ Project(Left)∘(L ⋉_{l.c=r.c} R) — with before() and after() differing only in join kind, so the proof is non-vacuous and reflects the real optimization. Crucially, the rule's defining precondition (right input unique on the join's right key) is genuinely encoded, not silently dropped: `scan("R", T, true)` adds a key on R's column 0 in the serialized schema, and the condition is an equi-join on precisely that column, matching `mq.areColumnsUnique(right, joinInfo.rightSet())`. The narrowing — 1-column-per-side equi-join, INNER variant only, identity left-only projection — is specific, honestly flagged in the SCOPE line, and is not an artifact of laziness: multi-column equi-join uniqueness is inexpressible in the current DSL (ScanMany cannot declare a key), and a fully uninterpreted join condition would over-generalize a claim that is not valid for non-functionally-determining conditions, so this is a genuine, non-degenerate special case of the source rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7198918
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 40293625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 860958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 399959
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18354459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 40374833
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74156250
  }
}
```
