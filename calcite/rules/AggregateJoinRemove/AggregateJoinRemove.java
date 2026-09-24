package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — the join is a LEFT join of two one-column scans, and the aggregate over it has exactly one group key referencing the left (preserved) input's column and no aggregate calls.
public record AggregateJoinRemove() implements RRule {
    static final RelRN left = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // Uninterpreted join condition over (L's column, R's column).
    static final RexRN joinCond = left.joinPred("join_cond", right);

    static final RelRN join = left.join(JoinRelType.LEFT, joinCond, right);

    // Before: DISTINCT of the left column taken over the LEFT join.
    // (An aggregate with a single group key and no aggregate calls is a
    //  DISTINCT projection of that key.)
    static final RelRN beforeAgg = new RelRN.Aggregate(
            join,
            Seq.of(join.field(0)),
            Seq.empty());

    // After: the join is dropped; the same DISTINCT of the left column is
    // taken over the left input alone.
    static final RelRN afterAgg = new RelRN.Aggregate(
            left,
            Seq.of(left.field(0)),
            Seq.empty());

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        return afterAgg;
    }
}
