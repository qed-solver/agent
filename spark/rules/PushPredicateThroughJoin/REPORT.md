# PushPredicateThroughJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** INNER join only; the filter splits into exactly one left-only, one right-only, and one common conjunct; single-column inputs


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2973-3088
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (a top-level Filter of three distinct uninterpreted conjuncts over an INNER join with its own uninterpreted join condition) and after() (each single-side conjunct pushed as a Filter on its input, the common conjunct ANDed into the join condition) are structurally different and encode exactly Spark's InnerLike branch of the Filter-over-Join case, so the proof is non-vacuous. Symbol sharing is used correctly, not coincidentally: the same predicate operators are re-bound to the pre-join columns, whose values are identical in the join row (if QED had treated them as independent symbols the equivalence would fail, so the success confirms correct column unification), and left/right/common/join-conjuncts are properly kept as four independent symbols. The restrictions — INNER join only (other join types in the source rule have a different, filter-remaining-on-top rewrite), exactly one conjunct per category, single-column inputs, and the implicit assumption that the common conjunct is absorbable into the join condition (no `others` filter left on top, which is semantically neutral under QED's bag model that doesn't model throwable/stateful expressions) — are genuine, specific, and honestly declared in the SCOPE line, so this is a faithful partial encoding of the real optimization. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6791542
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36580125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1136917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 512334
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18575083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36675542
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71225750
  }
}
```
