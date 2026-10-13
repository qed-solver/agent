# ProjectSetOpTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 68  **Verification rounds used:** 3
**Scope detail:** a 2-input UNION ALL over single-column, same-type inputs with the top projection being a single shared uninterpreted function F, whereas Calcite's rule is arbitrary-arity, any referenced-column subset, multiple projection expressions, and also the OVER branch (window semantics are out of QED's reach)


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectSetOpTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the core semantics of ProjectSetOpTranspose: a top-level uninterpreted projection F is pushed below a UNION ALL (correctly using `union(true, …)` to match the rule's `setOp.all` precondition), so before() = Project(F(x), UnionAll(L, R)) and after() = UnionAll(Project(F(x), L), Project(F(x), R)) — structurally distinct and non-vacuous. The symbol sharing is correct (one shared F, independent L and R), no preconditions are missing, and the SCOPE: PARTIAL line honestly and specifically names every restriction (2-input, single-column, single uninterpreted expression) relative to the original's arbitrary-arity/multi-expression/OVER-branch generality, making the proof a meaningful universal statement rather than a degenerate or coincidentally-provable artifact. ```

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
    "nanos": 856250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 488084
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
    "nanos": 1702916
  }
}
```
