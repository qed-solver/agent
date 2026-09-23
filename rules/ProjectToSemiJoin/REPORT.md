# ProjectToSemiJoin

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 60  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SemiJoinRule.java

Note: SemiJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectToSemiJoinRule` variant (not AggregateToSemiJoinRule, JoinOnUniqueToSemiJoinRule, JoinToSemiJoinRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** AGREE

The rule's validity rests on the right-side aggregate grouping by exactly the join keys, so its output is unique per key value — which is precisely what turns the inner join into a semi-join over the pre-aggregate input. QED's stated limitation is that it "knows nothing about a specific aggregate function's algebra beyond bag equality of its input": it cannot see through an aggregate to equate its grouped, unique-on-keys output with a semi-join over the raw input relation, and that is exactly the reasoning step this rule requires (the null hypothesis the porter chased is a red herring — both the inner-join-with-aggregate and the semi-join drop null-key rows identically). The porter's encoding was structurally sound (aggregate + inner join in before, semi-join in after, shared projection symbol over the same left columns) yet still returned a clean "not provable" with no panic or parse error, so the blocker is this fundamental aggregate-modeling gap in QED rather than a fixable symbol-sharing or nullability bug, and the DSL already exposes both `aggregate` and `JoinRelType.SEMI` so it is not a closable DSL-side gap either. ```
