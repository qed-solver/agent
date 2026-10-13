# SemiJoinFilterTranspose

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache Calcite
**Porter attempts used:** 16  **Verification rounds used:** 2
**Scope detail:** public record SemiJoinFilterTranspose() implements RRule {


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinFilterTransposeRule.java
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the original rule: before() is SemiJoin(Filter(X, F), Y, C) and after() is Filter(SemiJoin(X, Y, C), F), with the same uninterpreted filter predicate F over X's columns and the same uninterpreted join condition C over (X,Y) columns in both sides. The join kind is SEMI (matching the source rule's `Join::isSemiJoin` predicate and `JoinRelType.SEMI` construction), the tree structures are genuinely different (filter below vs. above the join), and the fixed 2-column arity on X and Y is not a meaningful restriction since all predicates are uninterpreted symbols—the rule's logical content is purely about tree shape, which is fully captured. No preconditions are missing, no symbols are incorrectly shared or independent, and `// SCOPE: FULL` is honest. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
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
    "nanos": 57375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 541917
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
    "nanos": 905333
  }
}
```
