package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// UNSUPPORTED
public record DivideByOne() implements RRule {
    @Override
    public RelRN before() {
        throw new UnsupportedOperationException();
    }

    @Override
    public RelRN after() {
        throw new UnsupportedOperationException();
    }
}
