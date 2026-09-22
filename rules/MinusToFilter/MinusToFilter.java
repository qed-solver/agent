package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// probe: single-filter variant to isolate NOT/complement behavior
public record MinusToFilter() implements RRule {
    static final RelRN base = RelRN.scan("Base", "Base_Type");
    static final RexRN q = base.pred("Q");

    @Override
    public RelRN before() {
        return base.minus(false, base.filter(q));
    }

    @Override
    public RelRN after() {
        RelRN f = base.filter(new RexRN.Not(q));
        return new RelRN.Aggregate(f, Seq.of(f.field(0)), Seq.empty());
    }
}
