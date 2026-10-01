package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — assumes the IN list is a plain disjunction of column-equality disjuncts, so the rule is captured as removing one repeated disjunct from the OR (the DSL has no IN node or literal constants).
public record InListDedup() implements RRule {
    static final RelRN source = RelRN.scan("Source", "V_Type");
    static final RexRN p1 = source.pred("p1");
    static final RexRN p2 = source.pred("p2");
    static final RexRN p3 = source.pred("p3");

    @Override
    public RelRN before() {
        return source.filter(new RexRN.Or(Seq.of(p1, p2, p2, p3)));
    }

    @Override
    public RelRN after() {
        return source.filter(new RexRN.Or(Seq.of(p1, p2, p3)));
    }
}
