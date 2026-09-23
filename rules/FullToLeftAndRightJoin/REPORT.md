# FullToLeftAndRightJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 11  **Verification rounds used:** 1
**Scope detail:** the join condition is a single equality (L.col = R.col) between one column of each side, an equijoin condition that is never TRUE on null-extended rows


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FullToLeftAndRightJoinRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding mirrors the source rule's exact shape — before() is the FULL join, after() is (LEFT join) UNION ALL ((RIGHT join) filtered by IS_NOT_TRUE of the same condition) — with the correct join kinds, the correct union-all flag, and the condition/field references shared consistently, since all three join outputs over L and R have the same 2-column (L||R) layout so field(0)=L.col and field(1)=R.col line up in both the join predicates and the filter. The only deviation is fixing the condition to a single equality L.col = R.col instead of an arbitrary deterministic predicate, which is a genuine and honestly-labeled PARTIAL restriction: the decomposition is only valid (and QED-provable) when the condition is never TRUE on null-extended rows, a property an uninterpreted predicate cannot satisfy under QED's no-predicate-inference semantics but a concrete equality does. The narrowing is specific, faithful, and non-degenerate (before and after remain structurally distinct, proving a real equijoin full-outer-join decomposition), so the "provable" result is meaningful. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 20219706
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 52608417
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 929917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1348042
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 42242500
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 52910334
  },
  "total_duration": {
    "secs": 0,
    "nanos": 111441042
  }
}
```
