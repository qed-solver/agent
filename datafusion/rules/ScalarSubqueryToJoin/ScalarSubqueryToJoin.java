package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.rex.RexSubQuery;

public record ScalarSubqueryToJoin() implements RRule {
    static final RelRN innerPlan = RelRN.scan("Inner", "Inner_Type").aggregate("s", "sum");
    static final RelRN outer = RelRN.scan("Outer", "Outer_Type");

    record Unique(RelRN input) implements RexRN {
        @Override
        public RexNode semantics() {
            return RexSubQuery.unique(input.semantics());
        }
    }

    @Override
    public RelRN before() {
        return outer.filter(new Unique(innerPlan));
    }

    @Override
    public RelRN after() {
        return outer;
    }
}
