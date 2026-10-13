# RewriteDistinctAggregates

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 25  **Verification rounds used:** 2
**Scope detail:** no grouping expressions and the distinct aggregate's argument is a literal constant (the only foldable case modeled)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/RewriteDistinctAggregates.scala, lines 199-437
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the source rule's no-grouping / foldable-argument special case (COUNT(DISTINCT literal)): before() is a single distinct aggregate and after() reproduces Spark's Expand→dedup-aggregate→outer non-distinct-count structure, where the single-projection Expand degenerates to a plain Project and the FILTER(gid=id) clause is vacuous (only one gid is ever emitted), so omitting it is sound. The two plans are structurally different and their aggregate input bags are not trivially identical (n constant copies vs. at most one deduplicated row), so the proof is non-vacuous. The scope is honestly tagged PARTIAL with a specific condition matching the rule's own foldable-children branch.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8792085
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 46192750
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1134916
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 699167
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22080292
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 46303792
  },
  "total_duration": {
    "secs": 0,
    "nanos": 84178209
  }
}
```
