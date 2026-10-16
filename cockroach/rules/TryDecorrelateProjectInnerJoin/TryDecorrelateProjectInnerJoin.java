package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — plain (non-Apply) LEFT/INNER joins over 1-col INTEGER scans; right Project is [pass, pass, f(pass, pass)]; on/innerOn/f uninterpreted
public record TryDecorrelateProjectInnerJoin() implements RRule {

    static final RelRN L = RelRN.scan("L", "INTEGER");
    static final RelRN IL = RelRN.scan("IL", "INTEGER");
    static final RelRN IR = RelRN.scan("IR", "INTEGER");

    static final RexRN innerOn = IL.joinPred("innerOn", IR);

    static final RelRN I = IL.join(JoinRelType.INNER, innerOn, IR);
    static final RexRN f = I.proj("f", "INTEGER");
    static final RelRN P = I.project(Seq.of(I.field(0), I.field(1), f));

    static final RexRN on = new RexRN.Pred(
            RuleBuilder.create().genericPredicateOp("on", true),
            L.joinFields(P, 0, 1, 2));

    @Override
    public RelRN before() {
        return L.join(JoinRelType.LEFT, on, P);
    }

    @Override
    public RelRN after() {
        RelRN I2 = IL.join(JoinRelType.INNER, RexRN.trueLiteral(), IR);
        RexRN f2 = I2.proj("f", "INTEGER");
        RelRN P2 = I2.project(Seq.of(I2.field(0), I2.field(1), f2));
        RexRN on2 = new RexRN.Pred(
                RuleBuilder.create().genericPredicateOp("on", true),
                L.joinFields(P2, 0, 1, 2));
        RexRN innerOn2 = new RexRN.Pred(
                RuleBuilder.create().genericPredicateOp("innerOn", true),
                L.joinFields(P2, 1, 2));
        return L.join(JoinRelType.LEFT, RexRN.and(on2, innerOn2), P2);
    }
}
