package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — only the local filter-pushdown fragment: a projection computing one uninterpreted expression over a pass-through column commuting below a filter whose predicate references that pass-through column only (the full DataFusion rule recurses through arbitrary plan shapes, splits/recombines projection lists, and handles Sort/Limit/Join plus filters referencing computed aliases).
public record PushDownLeafProjections() implements RRule {
    static final RelRN source = RelRN.scanMany("Source", Seq.of(
            RexRN.varType("A_Type", true)));

    // one shared computed expression (the "leaf" extraction projection)
    static final RexRN f = source.proj("F", "F_Type");

    @Override
    public RelRN before() {
        // Project(F(a), a)  over  Filter(P(a), Scan)
        // filter references the pass-through column a (in scope below the projection)
        RelRN filtered = source.filter(source.field(0).pred("P"));
        return filtered.project(Seq.of(f, source.field(0)));
    }

    @Override
    public RelRN after() {
        // Filter(P(a), Project(F(a), a) over Scan)
        // same predicate symbol P, now over the projection's pass-through column
        RelRN projected = source.project(Seq.of(f, source.field(0)));
        return projected.filter(projected.field(1).pred("P"));
    }
}
