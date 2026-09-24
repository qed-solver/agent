# DphypJoinReorder

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** a single concrete DpHyp-style reordering of a fully-connected 3-way inner-join tree (left-deep (A⋈B)⋈C to right-deep A⋈(B⋈C)) with single-column inputs and uninterpreted conjunctive join conditions, rather than an arbitrary cost-driven reordering of an arbitrary join tree


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/DphypJoinReorderRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The PARTIAL scope line correctly acknowledges that this is a 3-way, fully-connected, inner-join instance rather than the full cost-driven algorithm, and within that scope the encoding is faithful: the before and after are genuinely different join trees sharing the same uninterpreted pairwise conditions and output column order. The proof is therefore non-vacuous and does not rely on accidental over-constraint or a hidden precondition.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8479667
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 32807541
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 837291
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 538167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21662458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 32932917
  },
  "total_duration": {
    "secs": 0,
    "nanos": 80800834
  }
}
```
