package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the join kind is fixed to INNER (the rule applies to all join kinds, but QED models the join kind concretely, not as an uninterpreted symbol) and the join condition is a conjunction of an uninterpreted predicate with the constant FALSE
public record JoinReduceExpressions() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // One shared uninterpreted predicate symbol C over the join's two-column
    // row (L's column, R's column), used in both before() and after().
    static final RexRN c = left.joinPred("C", right);

    // Before: a join whose condition is the reducible expression C AND FALSE.
    @Override
    public RelRN before() {
        return left.join(JoinRelType.INNER, RexRN.and(c, RexRN.falseLiteral()), right);
    }

    // After: the same join, kept in place, with its condition reduced to the
    // constant FALSE literal — exactly what Calcite's
    // JoinReduceExpressionsRule.onMatch emits via
    // join.copy(traitSet, reducedCond, left, right, joinType, semiJoinDone);
    // no empty relation is introduced.
    @Override
    public RelRN after() {
        return left.join(JoinRelType.INNER, RexRN.falseLiteral(), right);
    }
}
