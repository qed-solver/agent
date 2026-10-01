# EmptyRequiredJoinSideToEmpty

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** the source rule's family covers INNER/LEFT/RIGHT/SEMI/ANTI/FULL joins whose required side is empty; this encoding pins the INNER join with an empty left side (both sides' shapes fixed to two columns).


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/propagate_empty_relation.rs, lines 75-175
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous and semantically exact for the branch it covers: before() is a real INNER join whose left input is a zero-row relation (DataFusion's `EmptyRelation { produce_one_row: false }`) with an uninterpreted join condition, and after() is a bare zero-row relation of the join's output schema (left++right), so the proved equivalence is precisely the source rule's `JoinType::Inner if left_empty` claim, for all instantiations of the uninterpreted types and predicate. The narrowing to INNER + empty-left (with two uninterpreted-typed columns per side) is explicitly and specifically disclosed in the SCOPE line, and it is a non-degenerate representative member: the rest of the source family is genuinely heterogeneous (other join types yield empty, the *surviving* side for the anti-join cases, or a null-padded projection — which needs a null literal the DSL does not yet expose) and cannot be captured by this single before/after pattern, so an honest partial rule is the right form here rather than a misleading claim of full generality.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 41583
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 112125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 265958
  }
}
```
