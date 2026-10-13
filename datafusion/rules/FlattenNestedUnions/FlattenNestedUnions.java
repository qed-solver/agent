package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — fixed-shape instance of the flatten: a 3-input UNION ALL with a nested 2-branch UNION ALL in the second input position, flattened to a 4-branch union (DataFusion's rule handles arbitrary nesting depth/arity, and also removes single-input unions and pushes projections down).
public record FlattenNestedUnions() implements RRule {
    static final RelRN a = RelRN.scan("A", "T");
    static final RelRN b = RelRN.scan("B", "T");
    static final RelRN c = RelRN.scan("C", "T");
    static final RelRN d = RelRN.scan("D", "T");

    @Override
    public RelRN before() {
        // Union(A, Union(B, C), D) — DataFusion's LogicalPlan::Union is a
        // bag (UNION ALL) n-ary union; a nested union branch arises when a
        // plan that is itself a union is unioned with other plans.
        return a.union(true, b.union(true, c), d);
    }

    @Override
    public RelRN after() {
        // Union(A, B, C, D) — the nested union flattened into a single union,
        // as OptimizeUnions produces via extract_plans_from_union (lines 63-74).
        return a.union(true, b, c, d);
    }
}
