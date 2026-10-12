# PushDownJoinThroughUnion

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** LeftOuter join, two-branch UNION ALL, single-column scans, one shared join-condition symbol C (source rule also covers INNER and arbitrary union arity; broadcast guards are cost-only)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushDownJoinThroughUnion.scala, lines 51-157
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, non-vacuous proof of a genuine special case of the Spark rule: `before` is `(X ∪+ Y) ⋈_LEFT O` and `after` is `(X ⋈_LEFT O) ∪+ (Y ⋈_LEFT O)`, which are structurally different and whose equality (union-all distributing over a left-outer join on the left side) is exactly the rewrite Spark performs. The symbol choices are the *correct* fidelity moves, not traps: reusing one condition symbol `C` across `condU/condX/condY` accurately models Spark reusing a single join condition (only attribute references are remapped per branch), and reusing one right-side scan `O` correctly abstracts Spark's `dedupRight` copy, which is bag-identical to the original. The narrowing to LeftOuter / 2-branch UNION ALL / single-column is real and disclosed in the `// SCOPE: PARTIAL` line (the source also covers INNER and arbitrary arity, and the broadcast guards are cost-only, not semantic preconditions), so the "provable" verdict is meaningful and not misleading — it is a true universal proof of a useful, non-degenerate subset rather than a vacuous `before()==after()` or an accidentally over-constrained pattern.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 22507374
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 61720834
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1098708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1331291
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 47873125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 62100208
  },
  "total_duration": {
    "secs": 0,
    "nanos": 126763125
  }
}
```
