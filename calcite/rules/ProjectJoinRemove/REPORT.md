# ProjectJoinRemove

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** the LEFT-join variant only, on a self-join of the same unique single non-nullable-column scan on equality of that column, with the project using only the preserved left column


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectJoinRemoveRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a non-vacuous, correctly-modeled special case of the source rule: it captures the LEFT-join self-join-on-unique-key form where projecting only the preserved column lets the join be eliminated, and QED must genuinely reason about uniqueness implying exactly-one-match (no null-extension, no duplicates) to establish equivalence with the plain scan projection. The SCOPE: PARTIAL line honestly and specifically names every restriction (LEFT-only, self-join, single non-nullable column, project on preserved column only), the uniqueness precondition is faithfully encoded via `unique=true`, and the concrete `EQUALS` condition is consistent with the source rule's requirement that join keys be extractable as equalities via `splitJoinCondition`.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5382750
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33518750
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 884833
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 551500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 15681291
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33593834
  },
  "total_duration": {
    "secs": 0,
    "nanos": 65425666
  }
}
```
