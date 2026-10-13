# FilterCalcMerge

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 11  **Verification rounds used:** 2
**Scope detail:** the input is fixed at 2 columns, the bottom Calc is assumed to have exactly one uninterpreted filter predicate over both columns and exactly two uninterpreted projection expressions over both columns, and the top is assumed to be a pure Filter with a single uninterpreted predicate over the bottom's two output columns


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/FilterCalcMergeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully models Filter-over-Calc (with condition B and projections [E0,E1]) as Filter(T)∘Project∘Filter(B) ⟹ Project∘Filter(T(E0,E1)∧B), which is exactly what RexProgramBuilder.mergePrograms produces (top identity program + condition T merged with the bottom program), and the symbol reuse is correct and required: the same T, the same projections E0/E1, and the same bottom condition B must appear on both sides. The source rule's two guards (no windowed aggregates in the Calc, no subquery in the Filter) are structurally excluded by the uninterpreted filter+project model, so no precondition is silently dropped, and before()/after() are genuinely different shapes (a real filter-merge, not a vacuous identity). The PARTIAL scope is honest and specific — fixed 2-column/2-projection arity and a one-condition/two-projection Calc, all with uninterpreted (not hard-coded) predicates and projections — and the result is a non-degenerate, universally-proven special case rather than a coincidental over-constraint. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5641707
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34713416
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 902625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 434250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16479208
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34820208
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67322500
  }
}
```
