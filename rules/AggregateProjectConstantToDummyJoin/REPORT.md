# AggregateProjectConstantToDummyJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 62  **Verification rounds used:** 3
**Scope detail:** the project emits exactly two boolean-literal constant columns and one non-constant column, and the aggregate groups by exactly those three columns with a single call on the non-constant one


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateProjectConstantToDummyJoinRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding mirrors the source rule's onMatch exactly — INNER join of the input with a one-row values table on constant true, a project restoring the original column order (constant columns re-sourced from the dummy relation, the identity column from the input), and an aggregate with the identical group set and the same uninterpreted aggregate "f" on the same column, so the proved equivalence is the genuine, non-vacuous transformation (before has no join/values, after does). The only shared symbol is the aggregate function name (intended symbol reuse), the join kind INNER is what the source hard-codes, concrete boolean literals are valid instances of RexLiteral since the rule's validity is independent of the literal values, and no precondition is missing (no uniqueness/NOT NULL is required by the source; literals are non-nullable on both sides, S is nullable on both). The SCOPE: PARTIAL tag is accurate and specific to the code (two boolean constant columns + one identity column, group by all three, one call), and since this DSL encodes fixed plan shapes rather than parametric arity, the narrowing is an honest, realizable, non-degenerate instance of the rule rather than a misleading one. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 11292168
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 39176708
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 972042
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 922834
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 27853042
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 39410292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 83528875
  }
}
```
