# MultiJoinProjectTranspose

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** the LEFT_PROJECT variant of the rule (a two-input join with


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MultiJoinProjectTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the LEFT_PROJECT variant's core rewrite — pulling a (pure, uninterpreted) projection up through an inner join, with the join condition's left-side reference rewritten from the project's output column (before: `leftProject.joinField(0,right)`) to the same projection recomputed over the raw base columns (after: `tlAfter`), and `tlOp`/`cOp` correctly shared so the proof is of the genuine commutation rather than a coincidental over-constraint. `before()` = `Join(C, Project(TL,L), R)` and `after()` = `Project(TL,r0,r1, Join(C',L,R))` are structurally distinct (project below vs. above the join) with matching (PL_Type,R0,R1) output schemas, so the proof is non-vacuous and the multiplicities line up under bag semantics. The restrictions (LEFT_PROJECT only, inner join, single uninterpreted projection over two base columns, base modeled as a scan) are genuine, specific, and honestly declared in the SCOPE line, making this a valid, non-degenerate PARTIAL encoding rather than a trivial or mis-scoped one. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7024792
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 36357209
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 874834
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 517500
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19071000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 36472583
  },
  "total_duration": {
    "secs": 0,
    "nanos": 71357916
  }
}
```
