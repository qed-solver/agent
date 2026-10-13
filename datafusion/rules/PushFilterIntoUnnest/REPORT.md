# PushFilterIntoUnnest

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 46  **Verification rounds used:** 4
**Scope detail:** one array column, one non-unnest conjunct, one unnest conjunct, and a single element column


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/push_down_filter.rs, lines 921-987
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's essence: the non-unnest conjunct P_NON is applied only to the input column and the unnest conjunct P_UNNEST only to the element column, mirroring DataFusion's column-reference-based split, and modeling Unnest as an INNER join under an uninterpreted membership predicate M correctly preserves the key semantics (empty array yields no rows) while letting QED prove the pushdown uniformly for all M, E, and conjuncts. Before and after are structurally distinct (P_NON above vs. below the join), so the proof is non-vacuous, the shared operator instances and join-field ordinals are all consistent across both sides, no side condition of the source rule is silently dropped, and the INNER-join output retaining the array column is harmless since it is present symmetrically on both sides and the real unnest output is just a projection of it. The flagged PARTIAL scope (one array column, one conjunct per side, no struct columns) is an honest, genuine special-case restriction — the two-conjunct split case still exercises the actual optimization and generalizes by conjunction to more conjuncts. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6710251
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34266458
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 901333
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 487792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18778042
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34371458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68859875
  }
}
```
