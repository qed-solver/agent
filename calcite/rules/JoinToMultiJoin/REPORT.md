# JoinToMultiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** only the INNER-join case (the real rule also refuses outer/null-producing inputs), fixed 3-way flattening of Join(C, X, MultiJoin(Y, Z, filter F_M)) into the flat 3-input MultiJoin with the combined filter F_M ∧ C


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinToMultiJoinRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core INNER-join 3-way flattening: `before` nests Fm at the inner join (Y⋈_Fm Z) with C at the outer level, while `after` lifts Fm to the root alongside C (Fm∧C) and makes the inner edge a true/cross-join — a structurally distinct but semantically equivalent pair (both yield {(x,y,z) | Fm(y,z)∧C(x,y,z)}), so the proof is non-vacuous. The SCOPE tag is honest and specific: the restriction to INNER (excluding LEFT/RIGHT/FULL where the real rule's null-generating-input logic prevents flattening) and to a fixed 3-way tree (vs. N-way) are genuine, meaningful special cases of the source rule, and all symbol sharing (Fm, C re-indexed correctly across the (x,y,z) layout) is correct.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 9083583
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34318375
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 866417
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 605125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 23594458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34432375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74062292
  }
}
```
