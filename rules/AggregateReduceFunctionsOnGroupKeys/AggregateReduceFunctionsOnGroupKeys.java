package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — probe
public record AggregateReduceFunctionsOnGroupKeys() implements RRule {
    static final RelRN source = RelRN.scan("S", "S_Type");
    static final RexRN g = source.groupBy("g");
    static final RelRN agg = new RelRN.Aggregate(source, Seq.of(g), Seq.of(source.field(0).aggCall("m")));

    @Override
    public RelRN before() {
        return agg;
    }

    @Override
    public RelRN after() {
        return agg.project(Seq.of(agg.field(0), agg.field(0)));
    }
}
