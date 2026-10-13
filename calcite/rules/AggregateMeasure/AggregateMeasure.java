package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// Calcite MeasureRules.AggregateMeasureRule:
//   Aggregate(R, [a,b], [AGG_M2V(c), SUM(d), AGG_M2V(e)])
// ==>
//   Aggregate(Project(R, [a,b,c,d,e, M2X(c,SP), M2X(e,SP)]), [f0,f1],
//             [SINGLE_VALUE(f5), SUM(f3), SINGLE_VALUE(f6)])
// where SP = SAME_PARTITION(a,b)
public record AggregateMeasure() implements RRule {
    // R carries exactly the five columns the rule references: keys a,b and operands c,d,e
    static final RelRN child = RelRN.scanMany("Child", Seq.of(
            new RelType.VarType("Child0_type", true),
            new RelType.VarType("Child1_type", true),
            new RelType.VarType("Child2_type", true),
            new RelType.VarType("Child3_type", true),
            new RelType.VarType("Child4_type", true)));

    static final RexRN a = child.field(0);
    static final RexRN b = child.field(1);
    static final RexRN c = child.field(2);
    static final RexRN d = child.field(3);
    static final RexRN e = child.field(4);

    // SAME_PARTITION(a, b): the measure's partition-key expression, uninterpreted
    static final RexRN samePartition = new RexRN.Proj(
            RuleBuilder.create().genericProjectionOp("SAME_PARTITION",
                    new RelType.VarType("PARTITION_type", true)),
            Seq.of(a, b));

    // M2X(c, SAME_PARTITION(a,b)) and M2X(e, SAME_PARTITION(a,b)), uninterpreted
    static final RexRN m2xC = new RexRN.Proj(
            RuleBuilder.create().genericProjectionOp("M2X",
                    new RelType.VarType("M2XC_type", true)),
            Seq.of(c, samePartition));

    static final RexRN m2xE = new RexRN.Proj(
            RuleBuilder.create().genericProjectionOp("M2X",
                    new RelType.VarType("M2XE_type", true)),
            Seq.of(e, samePartition));

    // Result-column types shared by the corresponding columns on both sides
    // (in Calcite, AGG_M2V(c) and SINGLE_VALUE(M2X(c,SP)) both have c's type)
    static final RelType m2vCType = new RelType.VarType("M2VC_type", true);
    static final RelType sumType  = new RelType.VarType("SUM_type", true);
    static final RelType m2vEType = new RelType.VarType("M2VE_type", true);

    @Override
    public RelRN before() {
        // Aggregate(R, [a, b], [AGG_M2V(c), SUM(d), AGG_M2V(e)])
        return new RelRN.Aggregate(child, Seq.of(a, b),
                Seq.of(new RelRN.AggCall("AGG_M2V", false, m2vCType, Seq.of(c)),
                        new RelRN.AggCall("SUM", false, sumType, Seq.of(d)),
                        new RelRN.AggCall("AGG_M2V", false, m2vEType, Seq.of(e))));
    }

    @Override
    public RelRN after() {
        // Aggregate(Project(R, [a,b,c,d,e,M2X(c,SP),M2X(e,SP)]), [f0,f1],
        //           [SINGLE_VALUE(f5), SUM(f3), SINGLE_VALUE(f6)])
        RelRN proj = child.project(Seq.of(a, b, c, d, e, m2xC, m2xE));
        return new RelRN.Aggregate(proj, Seq.of(proj.field(0), proj.field(1)),
                Seq.of(new RelRN.AggCall("SINGLE_VALUE", false, m2vCType, Seq.of(proj.field(5))),
                        new RelRN.AggCall("SUM", false, sumType, Seq.of(proj.field(3))),
                        new RelRN.AggCall("SINGLE_VALUE", false, m2vEType, Seq.of(proj.field(6)))));
    }
}