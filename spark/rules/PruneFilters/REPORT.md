# PruneFilters

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** only the constraint-pruning case where a conjunct of the outer filter is identical to a conjunct of the immediate child filter's predicate


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2510-2540
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() are structurally different in exactly the way Spark's rewrite acts — Filter(And(shared, restOuter), Filter(And(shared, restInner), R)) becomes Filter(restOuter, Filter(And(shared, restInner), R)) — so the proof's content is the non-vacuous absorption shared∧…∧shared∧… ≡ shared∧…, not a tautology. Symbol sharing is precisely right: "shared" being the same symbol in both filters models Spark's actual precondition (the child filter's conjunct appearing in the child's constraints, i.e. constraints.contains(cond)), while rest_inner/rest_outer correctly remain independent uninterpreted predicates, so no coincidental over-constraint or missing independence is inflating the result. The narrowing (child must itself be a Filter, one textually identical conjunct pruned, literal true/false branches omitted) is forced by QED's inability to model constraint propagation or entailment between independent symbols, it is honestly and specifically stated in the SCOPE line, and what remains is a genuine, common, non-degenerate instance of the real rule's constraint-pruning case (including its "deterministic conjunct" requirement, since all QED predicates are deterministic by construction). ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 3495916
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 6131500
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 49000
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 279958
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 7851667
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 6179625
  },
  "total_duration": {
    "secs": 0,
    "nanos": 16561750
  }
}
```
