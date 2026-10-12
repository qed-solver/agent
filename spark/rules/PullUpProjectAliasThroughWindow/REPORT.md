# PullUpProjectAliasThroughWindow

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** exactly one window in the chain, exactly one deterministic alias whose inputs are all window-referenced (retained) pulled from the bottom project to the top pass-through project, windows modeled as partition-determined per-partition uninterpreted aggregates joined back on a non-nullable partition key (same modeling as FoldGroupByAndWindow/CollapseWindow); multi-window chains, extra candidates and expression-consumed outputs are excluded (QED has no ordering/frame semantics for Window).


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/PullUpProjectAliasThroughWindow.scala, lines 64-140
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-trivial (before has a 3-col bottom project + identity top project; after has a 2-col bottom project + top project computing f(v)), correctly shares the uninterpreted symbols f and w across both sides, uses the right relational shape (Project→[Agg+InnerJoin+Proj]→Project) to model a single window, and captures the rule's key preconditions (determinism via uninterpreted projection, alias inputs ⊆ retained window-referenced columns, bare pass-through in top project, no expression consumption). The scope line honestly and specifically discloses the genuine narrowings: one window (QED lacks Window semantics), one alias, and a non-nullable partition key (required for the inner equi-join model to be sound).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 12537918
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 46190457
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1113291
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 1102291
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 28596375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 46470458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 91621500
  }
}
```
