package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import kala.collection.Seq;

// SCOPE: FULL
public record JoinExpandOrToUnion() implements RRule {
    static final RelRN l = RelRN.scan("L", "INTEGER");
    static final RelRN r = RelRN.scan("R", "INTEGER");
    static final RexRN c1 = l.joinPred("c1", r);
    static final RexRN c2 = l.joinPred("c2", r);

    @Override
    public RelRN before() {
        return l.join(JoinRelType.INNER, new RexRN.Or(Seq.of(c1, c2)), r);
    }

    @Override
    public RelRN after() {
        return l.join(JoinRelType.INNER, c1, r)
            .union(true, l.join(JoinRelType.INNER, RexRN.and(c2, new RexRN.Not(c1)), r));
    }
}
