package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the absorption is only applied when the false-AND conjunction is the filter condition
// DataFusion `false AND A --> false` (expr_simplifier.rs:1050-1055) is an
// expression-level rewrite valid in any boolean position; here it is
// instantiated at the filter condition, where filtering on `false AND right`
// keeps the same rows (none) as filtering on `false`.
public record AndFalseAbsorption() implements RRule {

    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN right = source.pred("right");

    @Override
    public RelRN before() {
        return source.filter(RexRN.and(RexRN.falseLiteral(), right));
    }

    @Override
    public RelRN after() {
        return source.filter(RexRN.falseLiteral());
    }
}
