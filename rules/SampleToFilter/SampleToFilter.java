package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

public record SampleToFilter() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN sampleCond = source.pred("P");
    static final RexRN randLessThanRate = source.pred("rand_lt_rate");

    @Override
    public RelRN before() {
        return source.filter(sampleCond);
    }

    @Override
    public RelRN after() {
        return source.filter(randLessThanRate);
    }
}
