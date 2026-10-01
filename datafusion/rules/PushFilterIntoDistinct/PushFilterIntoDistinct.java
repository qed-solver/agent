package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — only the Distinct::All branch is encoded (plain DISTINCT over all columns, modelled as group-by-all-columns with no aggregate calls) and the input has exactly two columns; the Distinct::On branch is not expressible because its "first row among duplicates" selection depends on ordering semantics that QED does not model.
public record PushFilterIntoDistinct() implements RRule {
    // The input relation of the Distinct node: plain DISTINCT deduplicates on
    // all of its columns, so its schema is exactly the dedup key set.
    static final RelRN input = RelRN.scanMany("Source", Seq.of(
            RexRN.varType("Col0_Type", false),
            RexRN.varType("Col1_Type", false)));

    // Plain DISTINCT(R) = group-by-all-columns with no aggregate calls
    // (set/dedup semantics: one row per distinct tuple).
    static RelRN distinct(RelRN r) {
        return new RelRN.Aggregate(r, r.fields(0, 1), Seq.empty());
    }

    @Override
    public RelRN before() {
        // Filter(p, Distinct(input)): filter applied after dedup, with the
        // predicate over the dedup output columns (= all input columns).
        RelRN d = distinct(input);
        return d.filter(d.pred("p"));
    }

    @Override
    public RelRN after() {
        // Distinct(Filter(p, input)): filter pushed below the dedup, with the
        // same predicate over the input columns.
        return distinct(input.filter(input.pred("p")));
    }
}
