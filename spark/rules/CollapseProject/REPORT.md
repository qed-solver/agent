# CollapseProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** 2-column input; the lower Project consists of exactly one uninterpreted expression E0(x, y) plus a pass-through of source column x, and the upper Project consists of exactly two uninterpreted expressions P0, P1 each referencing both lower outputs, all of which are inlined (no never/maybe-inline producers kept below)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 1648-2008
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful, honestly-tagged special case of CollapseProject's core branch: `before()` (nested `Project([P0(c0,c1),P1(c0,c1)], Project([E0(x,y),x], S))`) and `after()` (single `Project([P0(E0(x,y),x), P1(E0(x,y),x)], S)`) are structurally different plans whose equivalence is exactly the logical content of `buildCleanedProjectList` — alias references substituted by the alias body while the pass-through attribute is re-rooted directly on the source — with the same uninterpreted symbols (E0, P0, P1) correctly shared across both sides. The shape restrictions (2-column source, one alias + one pass-through below, two upper expressions each referencing both lower outputs, full inlining only) are genuine under-generalizations, are precisely and specifically stated in the `SCOPE: PARTIAL` line, and yield a non-degenerate rule; the source rule's physical side conditions (determinism, cheapness, Python-UDF eval types) govern execution cost/evaluation behavior and do not constrain bag semantics of pure uninterpreted functions, so no logical precondition is silently missing from the claim QED proved.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
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
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 390708
  }
}
```
