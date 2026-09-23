# AggregateToSemiJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 120  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `AggregateToSemiJoinRule` variant (not JoinOnUniqueToSemiJoinRule, JoinToSemiJoinRule, ProjectToSemiJoinRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rewrite is only valid because a GROUP BY over the right input yields exactly one row per distinct key, making `L INNER JOIN (right aggregate)` equivalent to `L SEMI JOIN (raw right)` — that dedup/partition fact is precisely aggregate algebra, which QED explicitly does not model beyond bag equality of an aggregate's input. Consequently the before-side top aggregate receives an input bag that includes the join's right columns while the after-side does not, and since aggregate calls are uninterpreted functions of their input bag, no encoding can make QED equate the two sides — the porter's "not provable" reflects a genuine QED limitation, not a fixable symbol-sharing or shape bug (the recorded HTTP 400 merely aborted a correctly diagnosed attempt).
