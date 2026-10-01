package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the containing left expression is assumed to be a single two-predicate OR (left = A OR B, right = A), rather than an arbitrary OR chain that contains A
public record OrSelfIdempotent() implements RRule {

    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN A = source.pred("A");
    static final RexRN B = source.pred("B");

    @Override
    public RelRN before() {
        return source.filter(new RexRN.Or(Seq.of(new RexRN.Or(Seq.of(A, B)), A)));
    }

    @Override
    public RelRN after() {
        return source.filter(new RexRN.Or(Seq.of(A, B)));
    }
}
