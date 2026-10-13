package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — the union has exactly two inputs (Calcite's rule fires on any number of inputs); both inputs are one-column relations of the same type, as required by Calcite's union type-checking.
public record UnionToDistinct() implements RRule {
    static final RelRN a = RelRN.scan("A", "T");
    static final RelRN b = RelRN.scan("B", "T");

    // Non-distinct union (all = true) of the same inputs.
    static final RelRN unionAll = a.union(true, b);

    @Override
    public RelRN before() {
        // UNION DISTINCT
        return a.union(false, b);
    }

    @Override
    public RelRN after() {
        // UNION ALL followed by distinct(): aggregate grouping by all output
        // columns, with no aggregate calls.
        return new RelRN.Aggregate(unionAll, Seq.of(unionAll.field(0)), Seq.empty());
    }
}
