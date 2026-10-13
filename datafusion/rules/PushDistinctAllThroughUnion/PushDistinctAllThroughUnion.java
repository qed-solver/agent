package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — the union has exactly two inputs (DataFusion's rule fires on any number of inputs); both inputs are one-column relations of the same type, as required by the union's schema.
public record PushDistinctAllThroughUnion() implements RRule {
    static final RelRN a = RelRN.scan("A", "T");
    static final RelRN b = RelRN.scan("B", "T");

    // Distinct(R) = group-by-all-columns with no aggregate calls (set/dedup semantics).
    static RelRN distinct(RelRN r) {
        return new RelRN.Aggregate(r, Seq.of(r.field(0)), Seq.empty());
    }

    @Override
    public RelRN before() {
        // Distinct(UNION ALL(Distinct(A), Distinct(B)))
        return distinct(distinct(a).union(true, distinct(b)));
    }

    @Override
    public RelRN after() {
        // Distinct(UNION ALL(A, B))
        return distinct(a.union(true, b));
    }
}
