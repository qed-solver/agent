# AggregateProjectMerge

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 68  **Verification rounds used:** 3
**Scope detail:** the aggregate's input is a two-column relation (two one-column scans inner-joined with a true condition), the project below the aggregate is the pure column swap, and the aggregate has one group key and one aggregate call, both over the same plain field reference


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a line-faithful instance of the source rule's `apply`: for the swap project `[x1, x0]` the interesting-field map is `{0→1}`, yielding `newGroupSet = {1}` and remapped call `f(1)`, and since the mapped key order `[1]` equals `newGroupSet.asList()` the rule emits no trailing project — exactly the `after()` given, while `before()` (Aggregate over Project over Join) differs structurally, so the proof is non-vacuous. The narrowings (two-column base as `A ⋈ B` because DSL scans are single-column, a bijective swap rather than an arbitrary input-ref project, one group key and one call with operand = key) are inherent to RuleScript's fixed-shape encodings, are accurately declared on the `// SCOPE: PARTIAL` line, and don't concretize anything that should be an uninterpreted symbol (the INNER/true join is mere base construction, absent from the real rule); symbol sharing (`f`, `A`, `B`) is correct, and the rule's input-ref precondition is satisfied by construction, so no failure mode (triviality, wrong operators, sharing error, missing precondition, dishonest scope) is present.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 13184332
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 44871709
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 966083
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 960125
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 30221209
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 45085167
  },
  "total_duration": {
    "secs": 0,
    "nanos": 91540333
  }
}
```
