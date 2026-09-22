package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// PROBE 1: pure projection-distinct identity, no aggregate calls.
public record AggregateJoinJoinRemove() implements RRule {
    static final RelRN L = RelRN.scan("L", "L_Type");
    static final RelRN M = RelRN.scan("M", "M_Type");
    static final RelRN R = RelRN.scan("R", "R_Type");

    static final SqlOperator PB = RuleBuilder.create().genericPredicateOp("PB", true);
    static final SqlOperator PT = RuleBuilder.create().genericPredicateOp("PT", true);

    static final RelRN bottomJoin = L.join(JoinRelType.LEFT,
            new RexRN.Pred(PB, Seq.of(L.joinField(0, M))), M);
    static final RelRN topJoin = bottomJoin.join(JoinRelType.LEFT,
            new RexRN.Pred(PT, Seq.of(bottomJoin.joinField(0, R), bottomJoin.joinField(2, R))), R);

    static final RelRN lrJoin = L.join(JoinRelType.LEFT,
            new RexRN.Pred(PT, Seq.of(L.joinField(0, R))), R);

    static final RelRN beforeAgg = new RelRN.Aggregate(topJoin,
            Seq.of(topJoin.field(0), topJoin.field(2)), Seq.empty());
    static final RelRN afterAgg = new RelRN.Aggregate(lrJoin,
            Seq.of(lrJoin.field(0), lrJoin.field(1)), Seq.empty());

    @Override
    public RelRN before() {
        return beforeAgg;
    }

    @Override
    public RelRN after() {
        return afterAgg;
    }
}
