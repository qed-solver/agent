# AggregateProjectPullUpConstants

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 63  **Verification rounds used:** 3
**Scope detail:** the aggregate's constant group key is a literal column emitted by a projection directly below the aggregate, not deduced from pulled-up predicates over an arbitrary input.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectPullUpConstantsRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's core transformation: an aggregate whose leading group key is a constant column is rewritten to an aggregate with that key dropped (group set kept non-empty, matching the rule's guard against an empty GROUP BY) plus a projection re-emitting the same literal in its original position, with the uninterpreted aggregate call shared unchanged and the original output column order preserved — and before/after are structurally different plans (2-key group-by vs 1-key group-by + project), so the proof is non-vacuous. Fixing the constant as a literal emitted by a Project directly below the aggregate is the only way to express "this column is constant" in QED (a pulled-up-predicate-deduced constant would require reasoning about a literal-equality constraint that the prover treats as opaque), so the stated PARTIAL scope is an honest, specific, genuine limitation rather than an avoidable under-generalization, and the specific literal value is immaterial since QED erases all types to integers. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 10933709
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 46940957
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1283792
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 815375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 26321875
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 47136458
  },
  "total_duration": {
    "secs": 0,
    "nanos": 89852292
  }
}
```
