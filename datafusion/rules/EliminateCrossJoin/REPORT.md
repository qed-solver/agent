# EliminateCrossJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1
**Scope detail:** single 2-input cross join; one conjunct of the filter (the join predicate) is moved into the join's ON condition and dropped from the filter (the general rule also handles n-way joins, OR-based key extraction, and multi-conjunct removal)


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/eliminate_cross_join.rs, lines 1-455
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core semantic step of the DataFusion rule—moving a join predicate from a surrounding Filter into the INNER join's ON clause and dropping it from the Filter—using uninterpreted predicates (join_eq, rest) over the full join row, which is the right abstraction since the bag-semantic equivalence holds for any predicate, not only equi-joins. The INNER join kind with a TRUE condition correctly models a cross join, the shared symbol join_eq is exactly what the rewrite intends (the same predicate relocates from filter to ON), and before()/after() are structurally distinct so the proof is non-vacuous; the SCOPE line honestly and specifically flags the narrowing to a 2-input, single-conjunct case (the general rule also re-associates n-way joins and extracts keys through OR). ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6869627
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34638625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 854000
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 438125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18926959
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34734666
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69143500
  }
}
```
