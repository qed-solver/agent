package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — INNER join only; the above filter is merged into the join condition rather than being classified by referenced side
public record FilterJoin() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // Shared uninterpreted predicate symbols over the full join output row
    // (L's columns then R's columns). Reusing the same name in before() and
    // after() tells QED the occurrences are the same symbol.
    static final RexRN joinCond = left.joinPred("join_cond", right);
    static final RexRN aboveFilter = left.joinPred("above_filter", right);

    @Override
    public RelRN before() {
        // Filter(f, Join(L, R, c))
        return left.join(JoinRelType.INNER, joinCond, right).filter(aboveFilter);
    }

    @Override
    public RelRN after() {
        // Join(L, R, c ∧ f) — for INNER join, the above filter merges into
        // the join condition.
        return left.join(JoinRelType.INNER, RexRN.and(joinCond, aboveFilter), right);
    }
}
