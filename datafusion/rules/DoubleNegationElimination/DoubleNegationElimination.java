package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record DoubleNegationElimination() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN pred = source.pred("pred");

    @Override
    public RelRN before() {
        return source.filter(new RexRN.Not(new RexRN.Not(pred)));
    }

    @Override
    public RelRN after() {
        return source.filter(pred);
    }
}