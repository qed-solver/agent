package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record PushFilterIntoSubqueryAlias() implements RRule {
    // The subquery alias's input: an arbitrary relation.
    static final RelRN source = RelRN.scanMany("Source", Seq.of(
            RexRN.varType("C0_Type", true),
            RexRN.varType("C1_Type", true),
            RexRN.varType("C2_Type", true)));

    // A SubqueryAlias relabels its input's schema (renaming/requalifying
    // columns by name) without changing any row value or multiplicity, so in
    // QED it is an identity projection of its input — same row schema, same
    // columns.
    static RelRN subqueryAlias(RelRN input) {
        return input.project(input.fields());
    }

    // The filter predicate: uninterpreted, over all columns of the row. The
    // source rule rewrites the predicate's column references from the
    // alias's schema to the inner schema via a by-name rename map, but that
    // rename leaves the predicate's semantics over row values unchanged, so
    // it is the same uninterpreted predicate above and below the alias.
    static final RexRN pred = source.pred("P");

    @Override
    public RelRN before() {
        // Filter(P, SubqueryAlias(Source))
        return subqueryAlias(source).filter(pred);
    }

    @Override
    public RelRN after() {
        // SubqueryAlias(Filter(P, Source))
        return subqueryAlias(source.filter(pred));
    }
}
