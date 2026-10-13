# ProjectRemove

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 41  **Verification rounds used:** 2
**Scope detail:** the identity project is over a single-column input (it projects its input's sole field); the original rule applies to any arity


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectRemoveRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully and non-vacuously captures the rule's core transformation: `before()` is a Project whose sole expression is `source.field(0)`, an input reference to field 0 of the same scan, which exactly embodies the `RexUtil.isIdentity` precondition in the pattern, and `after()` is the bare scan — so the proof is precisely the "remove an identity project" rewrite, not a `before() == after()` tautology. The arity-1 restriction is genuine (the DSL's `Project` carries a single expression and a `Scan` exposes exactly one column, so an n-ary identity projection is inexpressible without a DSL extension), is specifically and honestly tagged `SCOPE: PARTIAL`, and the instance remains a real, useful sub-case of the original any-arity rule (e.g., `SELECT a FROM t` → `t`) rather than a degenerate encoding.

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
    "nanos": 299417
  }
}
```
