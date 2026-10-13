package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the input has exactly two columns, which are all the columns the DISTINCT deduplicates on (fixed DSL scan arity), and only the main branch is encoded (Distinct over all columns ⟹ GROUP BY over all columns with no aggregate calls), while the zero-column LIMIT-1 and the unique-input drop-the-Distinct corner branches are not expressible here.
public record DistinctToGroupBy() implements RRule {
    // The input relation of the DISTINCT node: its schema is exactly the
    // set of columns DISTINCT deduplicates on (expand_wildcard over the full
    // input schema), so modelling the input as those columns loses nothing.
    static final RelRN input = RelRN.scanMany("Input", Seq.of(
            RexRN.varType("Col0_Type", false),
            RexRN.varType("Col1_Type", false)));

    @Override
    public RelRN before() {
        // DataFusion's Distinct(All(input)): every distinct input row appears
        // exactly once in the output, modelled as a set-variant
        // self-intersection.
        return input.intersect(false, input);
    }

    @Override
    public RelRN after() {
        // Aggregate(input, group by all columns, no aggregate calls):
        // one output row per distinct input tuple, with exactly the
        // grouping (i.e. all) input columns.
        return new RelRN.Aggregate(input, input.fields(0, 1), Seq.empty());
    }
}
