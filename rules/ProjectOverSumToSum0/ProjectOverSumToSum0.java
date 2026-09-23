package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// Diagnostic probe: the weakest possible core of ProjectOverSumToSum0Rule,
// with the OVER clause stripped entirely — same group-by, same input bag,
// only the aggregate operator name differs (SUM vs SUM0). If QED cannot
// prove this, the window-level rule certainly cannot be.
public record ProjectOverSumToSum0() implements RRule {
    static final RelRN source =
            RelRN.scanMany("Source", Seq.of(RexRN.varType("KeyType", true), RexRN.varType("ValueType", true)));
    static final RexRN groupExpr = new RexRN.GroupBy(
            RuleBuilder.create().genericProjectionOp("g", new RelType.VarType("KeyType", true)),
            Seq.of(source.field(0)));
    static final RelType valType = new RelType.VarType("ValueType", true);

    @Override
    public RelRN before() {
        return source.aggregate(groupExpr,
                new RelRN.AggCall("SUM", false, valType, Seq.of(source.field(1))));
    }

    @Override
    public RelRN after() {
        return source.aggregate(groupExpr,
                new RelRN.AggCall("SUM0", false, valType, Seq.of(source.field(1))));
    }
}