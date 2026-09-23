package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the filter condition is the constant FALSE literal (the branch where constant reduction of the condition yields a constant false, so the filter is replaced by an empty relation with the input's row type via createEmptyRelOrEquivalent)
public record FilterReduceExpressions() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    // Before: Filter(R, false) — the condition reduced to the constant FALSE literal.
    @Override
    public RelRN before() {
        return source.filter(RexRN.falseLiteral());
    }

    // After: empty relation with the input's row type,
    // mirroring createEmptyRelOrEquivalent: call.builder().push(input).empty().build()
    @Override
    public RelRN after() {
        return source.empty();
    }
}
