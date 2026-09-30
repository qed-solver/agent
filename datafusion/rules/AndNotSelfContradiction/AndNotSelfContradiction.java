package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record AndNotSelfContradiction() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN a = source.pred("A");

    @Override
    public RelRN before() {
        return source.filter(RexRN.and(a, new RexRN.Not(a)));
    }

    @Override
    public RelRN after() {
        return source.filter(RexRN.falseLiteral());
    }
}
