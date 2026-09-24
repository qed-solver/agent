package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — each join input has exactly one column, and the join condition is a single uninterpreted predicate over the join's two-column row
public record JoinCommute() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");
    // One shared uninterpreted predicate symbol P, used on both sides.
    static final SqlOperator joinCond = RuleBuilder.create().genericPredicateOp("join_cond", true);

    // P applied to (L value, R value): in Join(L, _, R) these are fields (0, 1).
    static final RexRN beforeCond = new RexRN.Pred(joinCond, Seq.of(
            left.joinField(0, right),
            left.joinField(1, right)));

    // In the swapped Join(R, _, L), L's column is at position 1 and R's at position 0;
    // the same P is still applied to (L value, R value), i.e. argument positions (1, 0).
    static final RexRN lCol = right.joinField(1, left);
    static final RexRN rCol = right.joinField(0, left);
    static final RexRN afterCond = new RexRN.Pred(joinCond, Seq.of(lCol, rCol));

    @Override
    public RelRN before() {
        return left.join(JoinRelType.INNER, beforeCond, right);
    }

    @Override
    public RelRN after() {
        return right.join(JoinRelType.INNER, afterCond, left)
                .project(Seq.of(lCol, rCol));
    }
}
