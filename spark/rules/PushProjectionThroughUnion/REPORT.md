# PushProjectionThroughUnion

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 2
**Scope detail:** two-branch UNION ALL, both inputs one shared fixed-width schema, single uninterpreted whole-row projection G (source fires on arbitrary arity, any deterministic projection list, and also SequentialStreamingUnion)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 1421-1481
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is structurally non-vacuous (Project(G, L ∪ R) vs Union(Project(G,L), Project(G,R))) and faithfully models the rule's core claim: a deterministic projection (here, one uninterpreted whole-row function G, whose function semantics automatically enforces Spark's `projectList.forall(_.deterministic)` precondition) commuting past a bag union, with the shared G symbol and positionally aligned identical schemas correctly mirroring Spark's `AttributeMap(left.output.zip(right.output))` remapping and union's left-child output types. The restrictions — two branches, fixed two-column width, single projection symbol, and omission of the exprId-duplicate/Alias-regeneration guards — are genuine DSL limitations or Spark plan-bookkeeping concerns rather than semantic preconditions (bag equivalence holds unconditionally), and they are specifically and honestly flagged in the PARTIAL scope line, so the provable result is a legitimate, non-degenerate special case of the source rule.

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
    "nanos": 1086500
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 536750
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
    "nanos": 1921750
  }
}
```
