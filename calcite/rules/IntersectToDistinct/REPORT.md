# IntersectToDistinct

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/IntersectToDistinctRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire correctness content is that a key survives the final `Filter(count_i > 0)` (no-pushdown variant) or `Filter(count(*) = N)` (pushdown variant) exactly when it appears in every branch — i.e. it rests on the identity that `COUNT(*)` (and a filtered `COUNT(*)`) of a group equals that group's cardinality and is positive iff the group is non-empty. QED models every aggregate call as an uninterpreted function of its input bag (equating two results only when their input bags are bag-equal) and has no count/cardinality algebra or constant folding, so it cannot relate the emitted count value to branch membership and cannot prove the tagged UNION ALL + GROUP BY + count-filter `after()` equivalent to the set-`INTERSECT` `before()`. This is a fundamental prover limitation (an aggregate algebraic identity QED cannot know), not a missing DSL operator — the DSL already exposes `aggregate`/`union`/`filter`/`project`/`intersect`, so `extend_dsl_file` cannot change what the trusted prover knows about `COUNT`, and no faithful encoding of this rule avoids the count-based membership test. ```
