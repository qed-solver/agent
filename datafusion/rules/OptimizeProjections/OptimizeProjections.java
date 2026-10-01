package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — only the local fragment pruning one unused uninterpreted expression from a two-layer projection above a fixed 3-column scan (the full rule also recurses through the plan, adds pre-join projections, and removes identity projections).
public record OptimizeProjections() implements RRule {
    static final RelRN source = RelRN.scanMany("Source", Seq.of(
            RexRN.varType("A_Type", true),
            RexRN.varType("B_Type", true),
            RexRN.varType("C_Type", true)));

    static final RexRN exprA = source.proj("FA", "FA_Type");
    static final RexRN exprB = source.proj("FB", "FB_Type");
    static final RexRN exprC = source.proj("FC", "FC_Type");

    @Override
    public RelRN before() {
        RelRN inner = source.project(Seq.of(exprA, exprB, exprC));
        return inner.project(Seq.of(inner.field(0), inner.field(1)));
    }

    @Override
    public RelRN after() {
        return source.project(Seq.of(exprA, exprB));
    }
}
