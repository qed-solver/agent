package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — assumes the filter predicate is exactly the TRUE literal rather than any predicate that simplifies to AcceptsAll
public record FilterAcceptsAllToNoop() implements RRule {
    // An arbitrary input relation standing in for the filter's child.
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    @Override
    public RelRN before() {
        // Filter whose predicate is the TRUE literal: it accepts every row.
        return source.filter(RexRN.trueLiteral());
    }

    @Override
    public RelRN after() {
        // The filter is a no-op, so it is eliminated, leaving its input.
        return source;
    }
}
