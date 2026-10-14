# MarkToSemiOrAntiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 45  **Verification rounds used:** 3
**Scope detail:** semi case only (marker conjunct kept true): single-column L and R, one residual filter conjunct P, one projected column p; the LEFT_MARK join is modeled as UNION ALL of the semi branch (marker=true) and the anti branch (marker=false)


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MarkToSemiOrAntiJoinRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The union-all expansion of LEFT_MARK into marker-tagged semi and anti branches is bag-exactly faithful, and before/after are structurally distinct (marker-filtered union vs. bare semi), so the proof is non-vacuous; cond, the residual predicate P, and the projection p are all uninterpreted and correctly shared, and the semi branch is the only case of the source rule with no side conditions, so no precondition is silently dropped. The scope line honestly discloses the genuine narrowing (semi case only, single-column inputs, single residual conjunct and projected column), and the proved statement remains a real, non-degenerate special case of the rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8975250
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36198125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1076958
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 904792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23012250
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36310375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 75350667
  }
}
```
