package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RRule;

// SCOPE: PARTIAL — uncorrelated EXISTS only (modeled as a left-semi join on a constant-true condition), with a single uninterpreted projection expression
public record EliminateExistsProject() implements RRule {
    // The outer relation whose rows each test the EXISTS subquery. An
    // uncorrelated EXISTS observes only the subquery's non-emptiness, so it is
    // modeled as a left-semi join with a constant-true condition: every outer
    // row is kept iff the subquery is non-empty.
    static final RelRN outer = RelRN.scan("Outer", "Outer_Type");

    // The subquery's base input.
    static final RelRN input = RelRN.scan("Input", "Input_Type");

    // A Project over that input: one uninterpreted projection expression. A
    // Project maps each input row to exactly one output row, so it never
    // changes row cardinality; and non-emptiness is the only thing EXISTS
    // cares about, so the Project is a no-op for EXISTS.
    static final RelRN project = input.project("p", "P_Type");

    @Override
    public RelRN before() {
        return outer.join(JoinRelType.SEMI, RexRN.trueLiteral(), project);
    }

    @Override
    public RelRN after() {
        return outer.join(JoinRelType.SEMI, RexRN.trueLiteral(), input);
    }
}
