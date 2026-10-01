package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — INNER join only, a single parent-predicate conjunct that references
// only the left input's columns is pushed from above the join down onto the left child
// (DataFusion push_down_join's left-only classification); the other categories
// (right-only, mixed/ON-condition, inferred predicates, OR-clause extraction,
// null-aware joins) are not modeled here.
public record PushFilterIntoJoin() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // The shared join condition (over both inputs' columns).
    static final RexRN joinCond = left.joinPred("join_cond", right);

    // The left-only filter. Expressed over the left input's column in each context:
    //  - above the join: the left column is join-field 0
    //  - on the left child: the left column is field 0 of L
    // Both resolve to L's actual column, so reusing the operator name "left_filter"
    // makes QED treat the two occurrences as the same uninterpreted predicate.
    static final RexRN leftFilterAboveJoin = left.joinField(0, right).pred("left_filter");
    static final RexRN leftFilterOnLeft = left.field(0).pred("left_filter");

    @Override
    public RelRN before() {
        // Filter(left_filter, Join(INNER, join_cond, L, R))
        return left.join(JoinRelType.INNER, joinCond, right).filter(leftFilterAboveJoin);
    }

    @Override
    public RelRN after() {
        // Join(INNER, join_cond, Filter(left_filter, L), R)
        return left.filter(leftFilterOnLeft).join(JoinRelType.INNER, joinCond, right);
    }
}
