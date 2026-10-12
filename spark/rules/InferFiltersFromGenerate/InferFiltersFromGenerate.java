package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the generate's child is a plain scan with no pre-existing constraints (so the rule's exclusion of the child's constraints is a no-op), and the row-multiplying Generate operator itself is modeled as an uninterpreted row-preserving projection since it has no bag-semantic meaning to QED; the proven effect is inserting the filter size(input)>0 AND input IS NOT NULL below the generate.
public record InferFiltersFromGenerate() implements RRule {
    // The generator's child (the rule only fires when it is a plain attribute).
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    // The generate's output, derived from the child's column (the single generator input):
    // an uninterpreted projection standing in for the unmodelable row-multiplying Generate.
    static final RexRN gen = source.proj("gen", "Gen_Type");
    // The two filters the rule infers from the generator (size(input) > 0, input IS NOT NULL),
    // introduced as independent uninterpreted predicates on the child's column.
    static final RexRN sizeNonEmpty = source.pred("size_gt_0");
    static final RexRN inputNotNull = source.pred("input_not_null");

    @Override
    public RelRN before() {
        // Generate(g, child): no filter under the generate.
        return source.project(gen);
    }

    @Override
    public RelRN after() {
        // Generate(g, Filter(size(input) > 0 AND input IS NOT NULL, child)).
        return source.filter(RexRN.and(sizeNonEmpty, inputNotNull)).project(gen);
    }
}
