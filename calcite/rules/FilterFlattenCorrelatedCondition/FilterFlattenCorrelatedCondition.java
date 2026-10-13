package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RelType;
import org.qed.RuleBuilder;

import kala.collection.Seq;

// SCOPE: PARTIAL — a single (uninterpreted) comparison between a correlated outer column and one uncorrelated computed expression over inner columns; the correlation context is modeled as an INNER cross-join of the outer scan and the inner scan, and the hoisted expression may not reference the outer columns
public record FilterFlattenCorrelatedCondition() implements RRule {
    static final RelRN outer = RelRN.scan("Outer", "T");
    static final RelRN inner = RelRN.scan("Inner", "T2");

    // Correlation context: an inner join with a true condition over (outer col o, inner col i)
    static final RelRN base = outer.join(JoinRelType.INNER, RexRN.trueLiteral(), inner);
    static final RexRN o = base.field(0);
    static final RexRN i = base.field(1);

    static final SqlOperator cmpOp = RuleBuilder.create().genericPredicateOp("Cmp", true);
    static final SqlOperator fOp = RuleBuilder.create()
            .genericProjectionOp("Expr", new RelType.VarType("T", true));

    // The hoisted uncorrelated call: an uninterpreted function of the inner column only,
    // producing a value of the same type as the outer column
    static final RexRN exprI = new RexRN.Proj(fOp, Seq.of(i));

    // Correlated comparison Cmp(o, Expr(i))
    static final RexRN cond = new RexRN.Pred(cmpOp, Seq.of(o, exprI));

    @Override
    public RelRN before() {
        // Filter(Cmp(o, Expr(i)), (o, i))
        return base.filter(cond);
    }

    @Override
    public RelRN after() {
        // Project([o, i, Expr(i)]).Filter(Cmp(f0, f2)).Project([f0, f1])
        RelRN projected = base.project(Seq.of(o, i, exprI));
        RexRN condAfter = new RexRN.Pred(cmpOp, Seq.of(projected.field(0), projected.field(2)));
        RelRN filtered = projected.filter(condAfter);
        return filtered.project(Seq.of(filtered.field(0), filtered.field(1)));
    }
}
