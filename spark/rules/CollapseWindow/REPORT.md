# CollapseWindow

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 49  **Verification rounds used:** 3
**Scope detail:** identical non-empty partition specs and identical order specs, one partition-determined window expression per window (modeled as uninterpreted per-partition aggregates), and the parent window's expression referencing only grandchild columns; the empty-order-spec merge directions are excluded (QED has no ordering semantics).


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2080-2152
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures CollapseWindow's core partition-determined subcase: each whole-partition-frame window is modeled as an uninterpreted per-partition aggregate joined back on the NOT-NULL partition key (the null-rejecting inner join is exactly why `k` is declared non-nullable, correctly avoiding the NULL-partition blowup that would break the join-back model), the parent's expression is computed over `base` to enforce the `references ∩ windowOutputSet = ∅` independence, and `before()` (two aggregates + two joins) is structurally distinct from `after()` (one two-call aggregate + one join), so the proof is a genuine universal aggregate-merge statement, not a vacuous identity. The omitted generality (differing/empty order-spec merge directions, bounded frames, row-dependent functions) rests on QED's lack of ordering/frame semantics — a fundamental prover limitation, not a DSL gap — and the `// SCOPE: PARTIAL` line honestly and specifically enumerates the assumptions (identical non-empty partition specs, identical order specs, one partition-determined expression per window, grandchild-only references, empty-order-spec directions excluded), making this a useful, non-degenerate special case.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 18942958
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36564458
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1036625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1687333
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 42549916
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36748917
  },
  "total_duration": {
    "secs": 0,
    "nanos": 97173209
  }
}
```
