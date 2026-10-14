package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — single filter conjunct, only the "extract(datepart, t) = const" form
public record FilterDateRange() implements RRule {
    // Single-column date table.
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    // Filter on the extracted date part, e.g. EXTRACT(YEAR FROM t) = 2020.
    // To QED this is an uninterpreted boolean predicate.
    static final RexRN extractEq = source.pred("extract_eq");

    // Range condition the rule derives via calendar arithmetic,
    // e.g. t >= DATE '2020-01-01' AND t < DATE '2021-01-01':
    // independent uninterpreted predicates, unrelated to extractEq.
    static final RexRN rangeLo = source.pred("range_lo");
    static final RexRN rangeHi = source.pred("range_hi");

    @Override
    public RelRN before() {
        return source.filter(extractEq);
    }

    @Override
    public RelRN after() {
        return source.filter(RexRN.and(rangeLo, rangeHi));
    }
}
