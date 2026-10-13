package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

import kala.collection.Seq;

// SCOPE: PARTIAL — INNER correlate only, modeled as an inner join on a true condition; non-correlated predicates are split into left-only (pushed to left), right-only (pushed to right), and both-sides (kept above)
public record FilterCorrelate() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    static final SqlOperator leftOp = RuleBuilder.create().genericPredicateOp("p_left", true);
    static final SqlOperator rightOp = RuleBuilder.create().genericPredicateOp("p_right", true);
    static final SqlOperator bothOp = RuleBuilder.create().genericPredicateOp("p_both", true);

    // Same uninterpreted predicate symbols in the standalone row context (after()).
    static final RexRN pLeft = new RexRN.Pred(leftOp, Seq.of(left.field(0)));
    static final RexRN pRight = new RexRN.Pred(rightOp, Seq.of(right.field(0)));

    // Same predicate symbols over the join output row (before()).
    static final RexRN pLeftJoin = new RexRN.Pred(leftOp, Seq.of(left.joinField(0, right)));
    static final RexRN pRightJoin = new RexRN.Pred(rightOp, Seq.of(left.joinField(1, right)));
    static final RexRN pBoth = left.joinPred(bothOp, right);

    @Override
    public RelRN before() {
        // Filter(p_left(L) AND p_right(R) AND p_both(L, R), Correlate(L, R, INNER))
        return left.join(JoinRelType.INNER, RexRN.trueLiteral(), right)
                .filter(RexRN.and(pLeftJoin, pRightJoin, pBoth));
    }

    @Override
    public RelRN after() {
        // Filter(p_both, Correlate(Filter(p_left, L), Filter(p_right, R), INNER))
        return left.filter(pLeft)
                .join(JoinRelType.INNER, RexRN.trueLiteral(), right.filter(pRight))
                .filter(pBoth);
    }
}
