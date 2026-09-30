package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RelType;

// SCOPE: PARTIAL — the redundant grouping column is a syntactic duplicate of another grouping column (trivial functional dependency)
public record ReduceGroupingCols() implements RRule {
    static final RelType.VarType kTy = RexRN.varType("K_Ty", true);
    static final RelRN input = RelRN.scanMany("Input", Seq.of(kTy));
    static final RexRN k = input.field(0);
    static final RelRN.AggCall sum = new RelRN.AggCall("Sum", false, kTy, Seq.of(k));
    static final RelRN.AggCall constAgg = new RelRN.AggCall("ConstAgg", false, kTy, Seq.of(k));

    @Override
    public RelRN before() {
        // GroupBy over $input with keys (k, k): the column k appears twice,
        // so the second occurrence is redundant (trivially determined by the first).
        return new RelRN.Aggregate(input, Seq.of(k, k), Seq.of(sum));
    }

    @Override
    public RelRN after() {
        // GroupBy over $input with key (k), the redundant column replaced by ConstAgg(k).
        return new RelRN.Aggregate(input, Seq.of(k), Seq.of(constAgg, sum));
    }
}
