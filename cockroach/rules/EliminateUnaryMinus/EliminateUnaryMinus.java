package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: FULL
public record EliminateUnaryMinus() implements RRule {
    static final RelRN input = RelRN.scan("Input", "Numeric_Type");

    @Override
    public RelRN before() {
        return input.project("neg", "Numeric_Type").project("neg", "Numeric_Type");
    }

    @Override
    public RelRN after() {
        return input;
    }
}