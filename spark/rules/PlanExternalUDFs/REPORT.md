# PlanExternalUDFs

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** single-column input and one scalar deterministic external UDF f applied to that column, extracted out of a Project operator into a dedicated lower UDF-evaluation projection that evaluates f once and passes the input column through (the upper operator keeps a non-UDF expression g).


## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/externalUDF/PlanExternalUDFs.scala, lines 41-271
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and faithful to the rule's core `extract` transformation: `before()` (Project computing f(x) inline alongside g(x)) and `after()` (a lower projection computing f(x) and passing x through — mirroring `ExecuteExternalUDF`'s result-plus-child-output shape — with the upper operator referencing the produced UDF column and keeping g over the pass-through column) are structurally different, and the proof verifies the genuine bag-equivalence π[f(x),g(x)] ≡ π[id,g∘id] ∘ π[f,id] rather than a trivial identity. Symbol handling is correct: f and g are distinct uninterpreted operators (matching the rule's UDF-vs-rest split), f is consistently shared between both plans as the same UDF, no constants, keys, or nullability assumptions are baked in, and the join-kind/union/aggregate shapes are correctly absent since the target case is a Project. The narrowing — single-column input, one deterministic UDF, Project as the host operator (vs. the rule's arbitrary operator, multiple-UDF recursion, and the separate join-condition/aggregate extraction paths) — is exactly what the SCOPE line states, so it is a genuine, specifically-stated, useful special case of the rule's logical core, which is the strongest claim QED (with no physical/external-execution semantics) can make about this rule.

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
    "nanos": 434542
  }
}
```
