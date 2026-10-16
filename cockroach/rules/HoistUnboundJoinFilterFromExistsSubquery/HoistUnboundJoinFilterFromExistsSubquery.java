// SCOPE: PARTIAL — the exists subquery's correlated join is encoded as a SEMI join between the single-column outer input and a single inner table, with one uninterpreted outer-only conjunct and one uninterpreted conjunct over both sides
package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

public record HoistUnboundJoinFilterFromExistsSubquery() implements RRule {
    // Outer input: its row is the correlation row of the exists subquery.
    static final RelRN input = RelRN.scan("A", "A_Type");
    // Inner table of the join inside the exists subquery.
    static final RelRN sub = RelRN.scan("K", "K_Type");

    // The unbound join filter as an outer-only filter (hoisted, on the after side).
    static final RexRN unboundCond = input.pred("P");
    // The same unbound conjunct as it appears inside the join condition (before side).
    static final RexRN joinUnbound = input.joinField(0, sub).pred("P");
    // The remaining join filter, uninterpreted over both join sides.
    static final RexRN joinCond = input.joinPred("Q", sub);
    // The outer select's other uninterpreted filter, over the outer input only.
    static final RexRN rest = input.pred("rest");

    @Override
    public RelRN before() {
        // Outer input SEMI-joined with the inner table on (P(A) AND Q(A, K)).
        return input.filter(rest)
                .join(JoinRelType.SEMI, RexRN.and(joinUnbound, joinCond), sub);
    }

    @Override
    public RelRN after() {
        // P(A) hoisted out of the join condition: filter first, then SEMI-join on Q(A, K).
        return input.filter(RexRN.and(unboundCond, rest))
                .join(JoinRelType.SEMI, joinCond, sub);
    }
}
