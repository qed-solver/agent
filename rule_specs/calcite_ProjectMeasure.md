# Name: ProjectMeasure
# Backend: Apache Calcite
# Source: core/src/main/java/org/apache/calcite/rel/rules/MeasureRules.java

Note: MeasureRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `ProjectMeasureRule` variant (not AggregateMeasureRule, AggregateMeasure2Rule, FilterSortMeasureRule, ProjectSortMeasureRule, which are separate rules ported under their own spec names).
