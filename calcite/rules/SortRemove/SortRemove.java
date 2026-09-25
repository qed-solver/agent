package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — probe: unconditional removal of an ORDER BY node (no offset/fetch)
public record SortRemove() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    @Override
    public RelRN before() {
        return source.sort(0);
    }

    @Override
    public RelRN after() {
        return source;
    }
}
