# AggregateRemoveDuplicateKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveDuplicateKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's essential step replaces a dropped group key with an ANY_VALUE aggregate call over the coarser grouping, so the proof requires ANY_VALUE(bag) to equal the unique key value on that group — but per the reference and the DSL source (aggregate ops are introduced only via uninterpreted genericAggregateOp), QED treats aggregates as uninterpreted with no algebra, so it can neither force that equality nor (via extend_dsl_file) model ANY_VALUE's constant-group identity. This is a fundamental QED limitation, not a missing DSL capability or an encoding bug, so no genuine RuleScript encoding of this rewrite (full or key-recovery special case) can be proved. ```
