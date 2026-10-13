package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — single 2-input cross join; one conjunct of the filter (the join predicate) is moved into the join's ON condition and dropped from the filter (the general rule also handles n-way joins, OR-based key extraction, and multi-conjunct removal)
public record EliminateCrossJoin() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // Uninterpreted predicates over the full join output row (L's column then R's column).
    static final RexRN joinEq = left.joinPred("join_eq", right);
    static final RexRN rest = left.joinPred("rest", right);

    @Override
    public RelRN before() {
        // Filter(join_eq AND rest, CrossJoin(L, R)) where CrossJoin = INNER join with TRUE condition
        return left.join(JoinRelType.INNER, RexRN.trueLiteral(), right)
                   .filter(RexRN.and(joinEq, rest));
    }

    @Override
    public RelRN after() {
        // Filter(rest, InnerJoin(L, R, join_eq))
        return left.join(JoinRelType.INNER, joinEq, right)
                   .filter(rest);
    }
}
