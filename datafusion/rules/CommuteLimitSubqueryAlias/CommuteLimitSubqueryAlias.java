package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rex.RexNode;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the limit's skip and fetch are fixed to the concrete literals 0 and 10 (Calcite's sortLimit builder rejects non-literal offset/fetch nodes, and QED's JSON carries RexNode literals)
public record CommuteLimitSubqueryAlias() implements RRule {
    // The subquery alias's input: an arbitrary relation.
    static final RelRN source = RelRN.scanMany("Source", Seq.of(
            RexRN.varType("C0_Type", true),
            RexRN.varType("C1_Type", true),
            RexRN.varType("C2_Type", true)));

    // A SubqueryAlias relabels its input's schema (renaming/requalifying
    // columns by name) without changing any row value or multiplicity, so in
    // QED it is an identity projection of its input.
    static RelRN subqueryAlias(RelRN input) {
        return input.project(input.fields());
    }

    // DataFusion's Limit { skip, fetch, input } has no ordering; in QED it is
    // a sort with an empty collation plus offset/fetch. The skip/fetch values
    // are the same on both sides of the rewrite; Calcite's sortLimit builder
    // requires literal (or dynamic-param) offset/fetch, so concrete values are
    // used. The commutation argument is uniform in them: they appear
    // identically on both sides, so the equivalence holds for any values.
    record Limit(int skip, int fetch, RelRN input) implements RelRN {
        @Override
        public RelNode semantics() {
            var builder = RuleBuilder.create();
            builder.push(input.semantics());
            return builder.sortLimit(skip, fetch, Seq.<RexNode>empty()).build();
        }
    }

    @Override
    public RelRN before() {
        // Limit(SubqueryAlias(Source))
        return new Limit(0, 10, subqueryAlias(source));
    }

    @Override
    public RelRN after() {
        // SubqueryAlias(Limit(Source))
        return subqueryAlias(new Limit(0, 10, source));
    }
}
