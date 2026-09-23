# ProjectCalcMerge

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 9  **Verification rounds used:** 1
**Scope detail:** fixed shape: 2-column input; the bottom Calc has one uninterpreted filter predicate over both input columns and exactly two uninterpreted projection expressions over both columns; the top Project has exactly two uninterpreted projection expressions over the bottom Calc's two output columns


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/ProjectCalcMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully desugars the Project-over-Calc match (a Calc as filter(B)∘project(E0,E1), exact because the fixed shape has no local variables, so filter-then-project order is precise) and the after() side is exactly what RexProgramBuilder.mergePrograms produces for a null-condition top program — the bottom's condition B unchanged and the top's P0, P1 with their references to the calc outputs translated to E0, E1 — with the same uninterpreted symbols (B, E0, E1, P0, P1) correctly shared between sides, matching how the real rule reuses the bottom's expressions. before() and after() are structurally different plan trees (extra intermediate project layer vs. inlined composition), so the proof is of the genuine merge identity, not a vacuous before==after, and the source rule's guards (no RexOver, no subqueries) are embodied by the DSL's per-row uninterpreted projection semantics rather than silently dropped. The fixed 2-column input, two bottom projections, and two top projections are an honestly and specifically tagged PARTIAL restriction imposed by the DSL's lack of symbolic arity, leaving a non-degenerate, useful special case of the rule.

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
    "nanos": 373958
  }
}
```
