package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: FULL
public record AndOrAbsorption() implements RRule {

    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN A = source.pred("A");
    static final RexRN B = source.pred("B");

    @Override
    public RelRN before() {
        return source.filter(RexRN.and(A, new RexRN.Or(Seq.of(A, B))));
    }

    @Override
    public RelRN after() {
        return source.filter(A);
    }
}
