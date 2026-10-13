package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — projection-only CSE instance: exactly two projected expressions sharing exactly one common subexpression
public record CommonSubexprEliminate() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN c0 = source.field(0);
    // the common subexpression g(x): computed once in the intermediate projection
    static final RexRN common = c0.proj("g", "CE_Type");
    // the two projected expressions f1(g(x)) and f2(g(x)) — distinct uninterpreted symbols
    static final RexRN e1 = common.proj("f1", "F1_Type");
    static final RexRN e2 = common.proj("f2", "F2_Type");

    @Override
    public RelRN before() {
        // P([f1(g(c0)), f2(g(c0))], S) — g(c0) evaluated twice
        return source.project(Seq.of(e1, e2));
    }

    @Override
    public RelRN after() {
        // intermediate projection: cache the common subexpression, pass through the input column
        RelRN inner = source.project(Seq.of(common, c0));
        RexRN t = inner.field(0);
        // rewritten projection: reference the cached column t = g(c0) instead
        return inner.project(Seq.of(t.proj("f1", "F1_Type"), t.proj("f2", "F2_Type")));
    }
}
