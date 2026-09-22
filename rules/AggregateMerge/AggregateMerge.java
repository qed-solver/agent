package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — the top aggregate has no aggregate calls, the bottom aggregate has exactly one aggregate call (which the merged aggregate drops), the top's single group key is exactly the bottom's first group key, the bottom has exactly two group keys, and the input is a one-column scan.
public record AggregateMerge() implements RRule {
    static final RelRN source = RelRN.scan("S", "S_Type");

    // Bottom aggregate: groups the source by two uninterpreted projections
    // k1, k2 and computes one aggregate call v, which the top aggregate
    // does not reference.
    static final RelRN bottom =
            new RelRN.Aggregate(
                    source,
                    Seq.of(source.groupBy("k1"), source.groupBy("k2")),
                    Seq.of(source.field(0).aggCall("v")));

    // Before: the top aggregate has no aggregate calls and groups by the
    // bottom's first output column (the k1 group key), so it only coarsens
    // the bottom's grouping to k1 while the bottom's v column is unused.
    static final RelRN top =
            new RelRN.Aggregate(bottom, Seq.of(bottom.field(0)), Seq.empty());

    // After: the original rule rebuilds the top aggregate over the
    // bottom's input, so the result is a single aggregate over the source
    // grouping only by k1, with the unused v call dropped.
    static final RelRN merged =
            new RelRN.Aggregate(source, Seq.of(source.groupBy("k1")), Seq.empty());

    @Override
    public RelRN before() {
        return top;
    }

    @Override
    public RelRN after() {
        return merged;
    }
}
