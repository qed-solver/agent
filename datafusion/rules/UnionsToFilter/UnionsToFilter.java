package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — exactly two branches over the same one-column source, each with a filter predicate, merged into one OR-filtered branch; DataFusion's rule also handles any arity, multi-column sources, projection/subquery-alias wrappers, and unfiltered (constant-true) branches.
public record UnionsToFilter() implements RRule {
    static final RelRN source = RelRN.scan("S", "T");
    static final RexRN p1 = source.pred("P1");
    static final RexRN p2 = source.pred("P2");

    // Distinct(R) = group-by-all-columns with no aggregate calls (set/dedup semantics).
    static RelRN distinct(RelRN r) {
        return new RelRN.Aggregate(r, Seq.of(r.field(0)), Seq.empty());
    }

    @Override
    public RelRN before() {
        // Distinct(UNION ALL(Filter(P1, S), Filter(P2, S)))
        final RelRN branch1 = source.filter(p1);
        final RelRN branch2 = source.filter(p2);
        return distinct(branch1.union(true, branch2));
    }

    @Override
    public RelRN after() {
        // Distinct(Filter(P1 OR P2, S))
        final RexRN cond = new RexRN.Or(Seq.of(p1, p2));
        return distinct(source.filter(cond));
    }
}
