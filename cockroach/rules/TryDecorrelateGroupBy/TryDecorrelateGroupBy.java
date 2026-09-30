package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.apache.calcite.rel.core.JoinRelType;
import kala.collection.Seq;

// SCOPE: PARTIAL — left is assumed to already have a strict key on all of its columns (modeled as a unique single-column scan), so no ordinality or CONST_AGG/ANY_NOT_NULL canary columns are synthesized and the DistinctOn variant is not covered.
public record TryDecorrelateGroupBy() implements RRule {

    static final RelRN left = RelRN.scan("Left", RexRN.varType("Left_Type", false), true);
    static final RelRN input = RelRN.scanMany("Input",
        Seq.of(RexRN.varType("Input_Group_Type", false),
               RexRN.varType("Input_Agg_Type", false)));

    static final RelRN groupBy = new RelRN.Aggregate(input,
        Seq.of(input.field(0)),
        Seq.of(input.field(1).aggCall("agg0")));

    @Override
    public RelRN before() {
        return left.join(JoinRelType.INNER, left.joinPred("on", groupBy), groupBy);
    }

    @Override
    public RelRN after() {
        RelRN cross = left.join(JoinRelType.INNER, RexRN.trueLiteral(), input);
        RelRN pushed = new RelRN.Aggregate(cross,
            Seq.of(cross.field(0), cross.field(1)),
            Seq.of(cross.field(2).aggCall("agg0")));
        return pushed.filter(pushed.pred("on"));
    }
}
