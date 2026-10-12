# MergeSubplans

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/core/src/main/scala/org/apache/spark/sql/execution/planmerging/MergeSubplans.scala, lines 151-341
```

## Independent verifier review

**Verdict:** AGREE

The rule's core content is a whole-plan common-subexpression elimination that identifies structurally identical but separately-instantiated scalar subplans and hoists them into new CTE definitions, and none of it is provable here: RuleScript rules carry no side conditions, so the "these two subplans are structurally equal" precondition cannot be stated (encoding them as one shared symbol makes the rewrite vacuous and unrepresentable, since the before/after shape depends on how many subqueries the input plan happens to contain), and the fixed QED arbiter's semiring/JSON language has no CTE definition/reference binding nor arbitrary scalar-subquery semantics (its subquery node serves only exists/in/unique higher-order operators), so the gap is in the arbiter, not a missing builder that `extend_dsl_file` could close; the rule's auxiliary one-row and determinism preconditions and the struct pack/unpack identities between independent uninterpreted functions are likewise beyond what QED can assume or know.
