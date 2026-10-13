package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — assumes the filter predicate is exactly the FALSE literal rather than any predicate that simplifies to RejectsAll (e.g. via AND with a false branch)
public record FilterRejectsAllToEmpty() implements RRule {
    // An arbitrary input relation standing in for the filter's child.
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    @Override
    public RelRN before() {
        // Filter whose predicate is the FALSE literal: it rejects every row.
        return source.filter(RexRN.falseLiteral());
    }

    @Override
    public RelRN after() {
        // The filter accepts no rows, so it is replaced by an empty relation
        // carrying the input's schema (EmptyRelation with produce_one_row = false).
        return source.empty();
    }
}
