package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — encodes A >> 0 -> A for A a single input column; the shift-by-zero is an uninterpreted scalar operator
public record BitwiseShiftByZeroNoop() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN value = source.field(0);

    @Override
    public RelRN before() {
        return source.project(value.proj("BitwiseShiftRightByZero", "Shifted_Type"));
    }

    @Override
    public RelRN after() {
        return source.project(value);
    }
}