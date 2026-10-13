package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RuleBuilder;
import org.qed.RexRN;

// SCOPE: PARTIAL — one-column scans A, B, C with both joins LEFT; the top join condition is a single uninterpreted predicate over (A's column, C's column), identically instantiated on both sides (index-shifted from fields (0,2) to (0,1) when the bottom join is removed); the bottom join condition is uninterpreted over (A, B); the aggregate is a pure DISTINCT group-by on (A's column, C's column) with no aggregate calls, mirroring the rule's "select distinct s.product_id, pc.product_id" example.
public record AggregateJoinJoinRemove() implements RRule {
    static final RelRN a = RelRN.scan("A", "A_Type");
    static final RelRN b = RelRN.scan("B", "B_Type");
    static final RelRN c = RelRN.scan("C", "C_Type");

    // Bottom join: A left join B on an uninterpreted condition over
    // (A's column, B's column). This join is the one the rule removes.
    static final RelRN bottom =
            a.join(JoinRelType.LEFT, a.joinPred("bottom_cond", b), b);

    // Top join condition: one uninterpreted predicate symbol, shared by both
    // sides, applied to (A's column, C's column). In the before-plan the row
    // space of (A left join B) left join C is (a, b, c), so A's column is
    // field 0 and C's column is field 2 (index 2 of the combined space, i.e.
    // field 0 of c). In the after-plan the row space of A left join C is
    // (a, c), so the same predicate sits on fields 0 and 1 — exactly the
    // source rule's RexUtil.shift(condition, leftBottomChildSize, -offset).
    static final SqlOperator topOp = RuleBuilder.create().genericPredicateOp("top_cond", true);
    static final RexRN topCondBefore =
            new RexRN.Pred(topOp, Seq.of(bottom.field(0), bottom.joinField(2, c)));
    static final RexRN topCondAfter =
            new RexRN.Pred(topOp, Seq.of(a.joinField(0, c), a.joinField(1, c)));

    // Before: DISTINCT(A.col, C.col) over (A left join B) left join C.
    static final RelRN beforeJoin = bottom.join(JoinRelType.LEFT, topCondBefore, c);
    static final RelRN beforeAgg = new RelRN.Aggregate(
            beforeJoin,
            Seq.of(beforeJoin.field(0), beforeJoin.field(2)),
            Seq.empty());

    // After: the bottom (A left join B) join is removed; A left join C with
    // the index-shifted condition, same DISTINCT group-by.
    static final RelRN afterJoin = a.join(JoinRelType.LEFT, topCondAfter, c);
    static final RelRN afterAgg = new RelRN.Aggregate(
            afterJoin,
            Seq.of(afterJoin.field(0), afterJoin.field(1)),
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
