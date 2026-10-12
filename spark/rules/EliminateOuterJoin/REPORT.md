# EliminateOuterJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** only the Filter-over-FullOuter-join → Inner-join branch of the rule's join-type case is modeled, with the null-rejecting predicates restricted to explicit IS_NOT_NULL on each side's column; the Aggregate and Project join-elimination cases and the other join-type branches are omitted


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/joins.scala, lines 158-238
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous (before = FULL outer join, after = INNER join with the identical filter left in place, exactly the source rule's `j.copy(joinType=...)` shape) and correctly shares `on`/`q`/the two `IS_NOT_NULL` conjuncts so the proof is of the real rewrite, not a coincidentally over-constrained one; the source's `canFilterOutNull` precondition is honored by using `IS_NOT_NULL`, the one null-rejecting form QED's semantics can universally verify (it cannot reason that an arbitrary uninterpreted predicate rejects null-extended rows), and the FULL→INNER case is the hardest of the join-type branches since it requires null-rejection on both sides. The SCOPE: PARTIAL tag is honest — it omits the Left/Right/one-sided-Full branches and all four Aggregate/Project elimination cases (and models each side as a single column), so this is a genuine, specific, non-degenerate special case rather than the full rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7594501
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
    "nanos": 1180667
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 907209
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21522625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 38215083
  }
}
```
