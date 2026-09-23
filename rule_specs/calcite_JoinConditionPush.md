# Name: JoinConditionPush
# Backend: Apache Calcite
# Source: core/src/main/java/org/apache/calcite/rel/rules/FilterJoinRule.java

Note: FilterJoinRule.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `JoinConditionPushRule` variant (not FilterIntoJoinRule (already ported separately as "FilterJoin"), which are separate rules ported under their own spec names).
