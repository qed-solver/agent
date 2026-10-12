# RemoveNoopOperators

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** only the no-op Project elimination sub-case is encoded, with the identity project over a single-column input (the rule's no-op Window elimination is inexpressible, since QED has no Window operator)


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 843-867
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous (before() contains a Project node that after() lacks), correctly captures the core no-op-Project semantic (identity projection ≡ child), and the single-column/scan-child restrictions are honest consequences of QED's fixed-schema, no-metavariable design rather than porter error; the PARTIAL scope line accurately documents both the Window sub-case exclusion (no Window operator in QED) and the arity limitation.

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
    "nanos": 452667
  }
}
```
