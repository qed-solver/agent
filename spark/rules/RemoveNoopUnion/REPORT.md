# RemoveNoopUnion

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 11  **Verification rounds used:** 2
**Scope detail:** the Distinct-like node is a plain Distinct, the UNION ALL has exactly two inputs, and both inputs are the identical relation (Spark's rule dedups canonically-equal children — modulo alias-only Projects — over any number of inputs).


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 873-921
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = Distinct(A UNION ALL A) and after() = Distinct(A) are structurally different yet genuinely equivalent (group-by-all dedup collapses the duplicated bag to the same set of distinct rows), so the proof is non-vacuous, and reusing the single scan `a` for both union children correctly models the rule's "canonically-equal children" precondition rather than over-constraining it. The encoding faithfully captures the core rewrite (collapse a no-op duplicate union under a Distinct-like node, matching the Spark trace Distinct→withNewChildren(simplifyUnion)→head), and is honestly and specifically scoped PARTIAL (plain Distinct, exactly two inputs, strictly identical children) while remaining a non-degenerate, useful special case of the broader canonicalization-based rule.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8902542
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 26555792
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1055833
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 473000
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22238375
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 26647459
  },
  "total_duration": {
    "secs": 0,
    "nanos": 64497084
  }
}
```
