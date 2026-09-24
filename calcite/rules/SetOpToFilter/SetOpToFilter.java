package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the 2-input UNION DISTINCT instance where both inputs are filters over the same single source with 2-valued predicates (Calcite's rule is general over arity, multiple distinct sources, and non-filter inputs).
public record SetOpToFilter() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN p1 = source.pred("P1");
    static final RexRN p2 = source.pred("P2");

    static final RelRN f1 = source.filter(p1);
    static final RelRN f2 = source.filter(p2);

    @Override
    public RelRN before() {
        // UNION DISTINCT of two filters over the same source
        return f1.union(false, f2);
    }

    @Override
    public RelRN after() {
        // SELECT DISTINCT ... FROM Source WHERE P1 OR P2
        // = aggregate (group-by all fields, no agg calls) over Filter(P1 OR P2, Source)
        RelRN filtered = source.filter(new RexRN.Or(Seq.of(p1, p2)));
        return new RelRN.Aggregate(filtered, Seq.of(filtered.field(0)), Seq.empty());
    }
}
