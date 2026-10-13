# OptimizeExpand

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 46  **Verification rounds used:** 3
**Scope detail:** Expand projections pass Key and Value through, vary only the two cat columns, and the inner distinct agg is a count over Value


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/OptimizeExpand.scala, lines 56-127
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous (after() inserts the dedup aggregate below the union-all-of-projections Expand, exactly the rule's rewrite) and the proven equivalence rests on the source rule's own correctness argument: the pure group-by sitting directly above the Expand erases row multiplicities because each branch output is a function of the input row, and the semantic precondition (all Expand-produced attributes are consumed by the inner group-by) is modeled, while the threshold/size checks in the source are performance heuristics, not semantic preconditions. Branch projections use distinct uninterpreted symbols per branch shared correctly across before()/after(), and the 3-branch/4-column/count-decomposition shape is a genuine, non-degenerate special case that is honestly and specifically tagged SCOPE: PARTIAL. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 126476374
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 89273708
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1561416
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 5075000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 240349000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 90682583
  },
  "total_duration": {
    "secs": 0,
    "nanos": 352421417
  }
}
```
