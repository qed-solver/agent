# PushdownPredicatesAndPruneColumnsForCTEDef

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** only the predicate-pushdown part (no column pruning), exactly two CTE references, both with pushable predicates


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PushdownPredicatesAndPruneColumnsForCTEDef.scala, lines 33-270
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding captures the rule's actual soundness core: the disjunction of the reference predicates is pushed into the shared CTE definition (`cte.filter(Or(pred1, pred2))`) while each reference keeps its own filter, with the union-all pairing the two independent uses of the same scan symbol — and before()/after() genuinely differ, so the proof is of the real invariant `filter(p, R) == filter(p, filter(p1∨p2, R))` per branch, not a vacuous identity. Predicates stay fully uninterpreted (no baked-in conditions), the shared scan symbol correctly models the single shared definition, and the determinism/non-forward-reference preconditions of the source rule are QED-expressible-impossible (non-determinism) rather than silently dropped. The `// SCOPE: PARTIAL` line is honest and specific — no column pruning, exactly two references, both pushable — and two references is the minimal case that makes the Or-pushdown non-degenerate, so the result is a useful genuine special case.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8270333
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 30818999
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1041167
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 540834
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21249125
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 30958583
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68432417
  }
}
```
