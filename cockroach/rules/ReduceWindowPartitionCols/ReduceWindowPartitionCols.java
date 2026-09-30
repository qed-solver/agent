package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;

// PROBE: does QED reduce a literal duplicate group-by key (k, k) to (k)?
public record ReduceWindowPartitionCols() implements RRule {
    static final RelType.VarType kT = RexRN.varType("k", false);
    static final RelType.VarType vT = RexRN.varType("v", false);

    static final RelRN wIn = RelRN.scanMany("In", Seq.of(kT, vT));

    // Group by (k, k) [literal duplicate] then drop the duplicate -> (k, W)
    static final RelRN beforeAgg = new RelRN.Aggregate(
            wIn,
            Seq.of(wIn.field(0), wIn.field(0)),
            Seq.of(wIn.field(1).aggCall("W")));
    static final RelRN before =
            beforeAgg.project(Seq.of(beforeAgg.field(0), beforeAgg.field(2)));

    // Group by (k) -> (k, W)
    static final RelRN after = new RelRN.Aggregate(
            wIn,
            Seq.of(wIn.field(0)),
            Seq.of(wIn.field(1).aggCall("W")));

    @Override
    public RelRN before() {
        return before;
    }

    @Override
    public RelRN after() {
        return after;
    }
}
