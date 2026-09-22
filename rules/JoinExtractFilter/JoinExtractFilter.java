package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: FULL
public record JoinExtractFilter() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // One shared uninterpreted predicate symbol C over the join's two-column
    // row (L's column, R's column).
    static final RexRN joinCond = left.joinPred("join_cond", right);

    @Override
    public RelRN before() {
        return left.join(JoinRelType.INNER, joinCond, right);
    }

    @Override
    public RelRN after() {
        // Same join, but cartesian (condition = true), with the condition
        // extracted as a filter on top. The filter's input row has the same
        // layout as the join output, so joinCond applies directly.
        return left.join(JoinRelType.INNER, RexRN.trueLiteral(), right).filter(joinCond);
    }
}