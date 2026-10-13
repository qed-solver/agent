# AggregateToSemiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 23  **Verification rounds used:** 2
**Scope detail:** INNER join only (not the rule's LEFT case), with both aggregates pure GROUP BY (zero aggregate calls) on single equi keys.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateToSemiJoinRule` variant (not JoinOnUniqueToSemiJoinRule, JoinToSemiJoinRule, ProjectToSemiJoinRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's INNER transformation — `GROUP BY l.k (L INNER-JOIN (GROUP BY r.k R) ON l.k=r.k)` rewritten to `GROUP BY l.k (L SEMI-JOIN R ON l.k=r.k)` — and before() and after() are genuinely structurally different (inner join over the grouped right vs. semi join over the raw right), so the provable result is non-vacuous and the equi-key, "join keys = right group columns," and INNER-jointype preconditions are all correctly represented; the narrowing to INNER-only, pure group-by (zero agg calls), and single-key is honestly disclosed in the PARTIAL scope line while remaining a meaningful, non-degenerate instance of the real rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 18802791
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36925417
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 891625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 647291
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 39677458
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 37206250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 92398292
  }
}
```
