package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — no grouping columns (ScalarGroupBy), where the per-call aggregate FILTER is encoded as a relational Filter below the aggregate (equivalent for the single group), and the aggregate argument is the scan's unique key.
public record EliminateAggFilteredDistinctForKeys() implements RRule {
    static final RelRN source = RelRN.scan("S", RexRN.varType("S_Type", false), true);
    static final RexRN x = source.field(0);
    static final RexRN p = source.pred("p");
    static final RelRN filtered = source.filter(p);

    @Override
    public RelRN before() {
        return new RelRN.Aggregate(filtered, Seq.empty(),
            Seq.of(new RelRN.AggCall("f", true, RexRN.varType("F_Type", true), Seq.of(x))));
    }

    @Override
    public RelRN after() {
        return new RelRN.Aggregate(filtered, Seq.empty(),
            Seq.of(new RelRN.AggCall("f", false, RexRN.varType("F_Type", true), Seq.of(x))));
    }
}
