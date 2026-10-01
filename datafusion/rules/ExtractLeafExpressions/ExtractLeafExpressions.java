package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — only the Filter-parent case with exactly one extracted sub-expression over a single-column input: Filter(P(e(x)), R) is rewritten by projecting e(x) into a new column below the filter, filtering on that column, and dropping it with a final projection so the output schema is unchanged.
public record ExtractLeafExpressions() implements RRule {
    static final RelRN source = RelRN.scan("R", "R_Type");

    // The leaf sub-expression e(x): an uninterpreted projection symbol applied
    // to the input's single column, shared between before() and after().
    static final RexRN expr = new RexRN.Proj(
            RuleBuilder.create().genericProjectionOp("e", RexRN.varType("Y_Type", true)),
            Seq.of(source.field(0)));

    // Before: the predicate P(e(x)) is evaluated inline in the filter.
    static final RexRN inlinePred = new RexRN.Pred(
            RuleBuilder.create().genericPredicateOp("P", true),
            Seq.of(expr));
    static final RelRN before = source.filter(inlinePred);

    // After: e(x) is extracted into a projection below the filter
    // (cols: 0 = e(x) of Y_Type, 1 = x of R_Type), the filter now
    // references the materialized column, and a top projection drops
    // the extracted column to restore the original single-column output.
    static final RelRN extraction = source.project(Seq.of(expr, source.field(0)));
    static final RelRN filtered = extraction.filter(extraction.field(0).pred("P"));
    static final RelRN after = filtered.project(Seq.of(filtered.field(1)));

    @Override
    public RelRN before() {
        return before;
    }

    @Override
    public RelRN after() {
        return after;
    }
}
