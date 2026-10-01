package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — models the rule as transposing one cheap and one expensive conjunct in a Filter's conjunction (the minimal non-sorted two-conjunct case) rather than stably partitioning an arbitrary-length conjunct list, and omits the backend's firing-only guards (volatile-predicate check, cheap-node allow-list) which have no bag-semantic content
public record ReorderPredicatesByCost() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN cheap = source.pred("cheap");
    static final RexRN expensive = source.pred("expensive");

    @Override
    public RelRN before() {
        return source.filter(RexRN.and(expensive, cheap));
    }

    @Override
    public RelRN after() {
        return source.filter(RexRN.and(cheap, expensive));
    }
}
