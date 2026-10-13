# JoinPushThroughJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** single-column table inputs, and both join conditions are a fixed representative conjunction of uninterpreted conjuncts (SA, SB, ST, TC) split by B-reference, not an arbitrary condition decomposition


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinPushThroughJoinRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the RIGHT instance of JoinPushThroughJoin: before is (A ⋈_{SA∧SB} B) ⋈_{ST∧TC} C with layout (A,B,C), and after is (A ⋈_{SA∧TC} C) ⋈_{ST∧SB} B followed by a project restoring (A,B,C) — exactly the conjunct redistribution Calcite's onMatchRight performs (SA/TC to the new A⋈C bottom, ST/SB to the new top), with both joins INNER as the rule requires, correct JoinField ordinals on every side, and consistent predicate-argument types so QED unifies the moved symbols correctly. The equivalence is a genuine, non-vacuous inner-join re-association identity (conjunction commutes over the a/b/c row variables), so before() and after() are structurally different yet truly bag-equivalent, and no symbol is shared where two independent predicates are needed. The PARTIAL scope is honest and specific — single-column scans (forced by the current `RelRN.scan`, which always yields one column) and a fixed representative conjunction split by B-reference rather than an arbitrary decomposition (which QED cannot express, since it has no notion of an uninterpreted condition partitioned by column reference) — so the narrowing is a real QED/DSL limitation, not an avoidable under-generalization, and the result remains a meaningful, non-degenerate instance of the rule. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8595790
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 39586333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 923542
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 600042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22185083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 39703417
  },
  "total_duration": {
    "secs": 0,
    "nanos": 101742000
  }
}
```
