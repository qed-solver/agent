package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// Confirms the core step of CleanupDynamicPruningFilters is NOT a logical
// equivalence in QED's bag-semantic model: the rule replaces a
// DynamicPruning conjunct with TrueLiteral when the same pruning key already
// has an equality-with-constant conjunct (e.g. `key = const AND dpp(key)` ->
// `key = const`). `dpp(key)` is really `key IN <dpp-values>` where
// <dpp-values> is a backend-internal, dynamically computed set. For an
// uninterpreted `dpp`, dropping it given only `key = const` is unsound (dpp
// may be false when key = const), so no DSL operator can manufacture the
// missing `key = const  =>  key IN <dpp-values>` entailment.
public record CleanupDynamicPruningFilters() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    // stands in for the equality-with-constant conjunct on the pruning key
    static final RexRN eq = source.pred("eq");
    // stands in for the DynamicPruning predicate key IN <dpp-values>
    static final RexRN dpp = source.pred("dpp");

    @Override
    public RelRN before() {
        return source.filter(RexRN.and(eq, dpp));
    }

    @Override
    public RelRN after() {
        return source.filter(eq);
    }
}