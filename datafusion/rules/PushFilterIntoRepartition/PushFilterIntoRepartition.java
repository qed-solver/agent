package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL — the repartition is modeled by its exact bag semantics: a schema-preserving pass-through that redistributes (permutesh) the input's rows without changing them or their multiplicities, which in QED is an identity projection; the filter predicate is a single uninterpreted predicate over the whole row.
public record PushFilterIntoRepartition() implements RRule {
    // The repartition's input: an arbitrary relation.
    static final RelRN source = RelRN.scanMany("Source", Seq.of(
            RexRN.varType("C0_Type", true),
            RexRN.varType("C1_Type", true),
            RexRN.varType("C2_Type", true)));

    // A Repartition passes every input row through unchanged (its output bag is
    // exactly the input bag, re-distributed across partitions), so in QED it is
    // an identity projection of its input — same row schema, same column names.
    static RelRN repartition(RelRN input) {
        return input.project(input.fields());
    }

    // The filter predicate: uninterpreted, over all columns of the row — the
    // predicate references the same logical columns above and below the
    // repartition since it preserves the schema.
    static final RexRN pred = source.pred("P");

    @Override
    public RelRN before() {
        // Filter(P, Repartition(Source))
        return repartition(source).filter(pred);
    }

    @Override
    public RelRN after() {
        // Repartition(Filter(P, Source))
        return repartition(source.filter(pred));
    }
}
