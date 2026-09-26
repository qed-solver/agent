package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the GroupBy's input is a base table whose single column is statically known to be a strict key (declared unique), so each group holds at most one row, and the sole first-non-NULL-style aggregate operates over that unique key column.
public record EliminateGroupBy() implements RRule {
    static final RelRN source = RelRN.scan("S", RexRN.varType("S_Type", false), true);
    static final RexRN key = source.field(0);
    static final RelRN.AggCall agg = new RelRN.AggCall("f", false, RexRN.varType("F_Type", true), Seq.of(key));

    @Override
    public RelRN before() {
        return new RelRN.Aggregate(source, Seq.of(key), Seq.of(agg));
    }

    @Override
    public RelRN after() {
        return source.project(Seq.of(key, key));
    }
}
