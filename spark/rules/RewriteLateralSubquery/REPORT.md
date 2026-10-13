# RewriteLateralSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 52  **Verification rounds used:** 4
**Scope detail:** fixed LEFT join kind, exactly one uninterpreted condition shared by the Correlate and the Join (no separate non-correlation condition), and the subquery side is an unchanged scan (no DomainJoin rewriting).


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 1090-1099
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and symbol-correct: `before()` is a `Correlate(LEFT, Filter(lateral_cond, S))` while `after()` is a plain `Join(LEFT, lateral_cond)`, and the shared predicate is applied to the identical (L,S) argument list in both (verified against `joinFields`/`JoinField` semantics), so the proof captures the rule's genuine core claim — per-row correlated evaluation against a correlation predicate is bag-equivalent to a plain join using that predicate as the join condition. The restrictions versus the full Spark rule (LEFT join only, no separate non-correlation condition, subquery side is an unchanged scan so `rewriteDomainJoins` is a no-op) are real, specific, and accurately stated in the `SCOPE: PARTIAL` line, and none of them degenerates the rule into a structural identity; the fixed join kind in particular reflects a DSL gap (no meta-join-type for `Correlate`) rather than a modeling error.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12875083
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 42641334
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1159666
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 896917
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30161083
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 42878375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 89330959
  }
}
```
