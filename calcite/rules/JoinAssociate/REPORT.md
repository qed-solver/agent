# JoinAssociate

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 34  **Verification rounds used:** 2
**Scope detail:** the join condition is a fixed representative split into uninterpreted conjuncts over the single-column scans, not an arbitrary condition decomposition


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinAssociateRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly re-associates ((A⋈B)⋈C)→(A⋈(B⋈C)) with INNER joins, faithfully splitting conditions by A-reference (PAB/PABC on top, PB/PBC/PC moved to the new bottom B⋈C), with all five uninterpreted predicate symbols consistently shared and correctly re-indexed across both trees; the PARTIAL scope is honest and specific—the fixed conjunct split and single-column scans reflect a genuine DSL limitation (column-reference-based decomposition can't be quantified in bag semantics), and the instance is non-degenerate, exercising conjuncts moving from both levels.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8420459
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34995958
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 845042
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 564500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21865625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35121958
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72733875
  }
}
```
