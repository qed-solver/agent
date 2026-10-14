package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

public record AggregateReduceFunctionsOnGroupKeys() implements RRule {
    static final RelRN source = RelRN.scan("S", "S_Type");
    static final RexRN key = source.field(0);

    @Override
    public RelRN before() {
        return new RelRN.Aggregate(source, Seq.of(key), Seq.of(key.aggCall("max")));
    }

    @Override
    public RelRN after() {
        final RelRN inner = new RelRN.Aggregate(source, Seq.of(key), Seq.empty());
        return inner.project(Seq.of(inner.field(0), inner.field(0)));
    }
}
