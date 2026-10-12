# GroupBasedRowLevelOperationScanPlanning

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 6  **Verification rounds used:** 1
**Scope detail:** the command-condition conjuncts fully evaluated by the data source are pushed


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/datasources/v2/GroupBasedRowLevelOperationScanPlanning.scala, lines 40-140
```

## Independent verifier review

**Verdict:** CONFIRMED

before() and after() are genuinely structurally different (the target-only conjuncts live inside the join condition vs. in a filter above the target scan), so the proof is non-vacuous, and the encoding faithfully models the rule's core MERGE step: the fully-evaluated command-condition conjuncts (uninterpreted eval1/eval2, correct symbol reuse across both sides) are pushed into the target scan and dropped from the condition, with the uninterpreted both-sided `rest` standing in for the surviving condition — right operators (scan/filter/inner-join), no concrete predicates, and no conflated symbols. The INNER-join restriction is a genuine, honest PARTIAL scope: dropping these conjuncts is bag-equivalent only for inner joins (an outer join would preserve unmatched left rows that fail eval1/eval2, breaking equality), while the source rule's `Join(...)` pattern leaves the join type unconstrained, so the narrower provable subset is correctly and specifically documented, and the rule remains non-degenerate. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7323251
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35292792
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1135292
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 473666
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19823375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35404833
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71342834
  }
}
```
