package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — assumes only a pair of consecutive filters over a shared base relation (local merge); excludes the source's conjunct simplification/reordering and the Limit/Offset-guarded join-pushdown branch.
public record MergeConsecutiveFilters() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN inner = source.pred("inner");
    static final RexRN outer = source.pred("outer");

    @Override
    public RelRN before() {
        return source.filter(inner).filter(outer);
    }

    @Override
    public RelRN after() {
        return source.filter(RexRN.and(inner, outer));
    }
}
