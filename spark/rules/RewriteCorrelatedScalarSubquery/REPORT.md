# RewriteCorrelatedScalarSubquery

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 62  **Verification rounds used:** 4
**Scope detail:** single-column relations on one equality correlation key with a single aggregate grouped by the outer key (no HAVING, projections, or multiple subqueries), with the aggregate kept above the join on both sides


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/subquery.scala, lines 698-1085
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a narrow but honest partial instance: for an outer aggregate whose scalar subquery is a single equality-correlated, non-aggregated one-row scan, Catalyst's rewrite reduces to replacing the correlated left join with a plain left join, which is exactly what is proved. It is non-vacuous because before uses Correlate and after uses Join, and the PARTIAL scope accurately states the omitted HAVING, projection, multi-subquery, and count-bug generality.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 28557372
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 85433959
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1172667
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1755167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 56529000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 86044125
  },
  "total_duration": {
    "secs": 0,
    "nanos": 159579250
  }
}
```
