package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

public record NormalizeJoinAnyFilter() implements RRule {
    static final RelRN left = RelRN.scan("Left", "T");
    static final RelRN right = RelRN.scan("Right", "T");
    static final RelRN sub = RelRN.scan("Sub", "T");
    static final RexRN a = left.field(0);

    @Override
    public RelRN before() {
        RexRN e1 = RexRN.exists(sub, a);
        RexRN e2 = sub.exists(a);
        RexRN e3 = sub.any(a);
        RexRN e4 = RexRN.any(sub, a);
        RexRN e5 = RexRN.any(a, sub);
        RexRN e6 = sub.in(a);
        RexRN e7 = RexRN.in(a, sub);
        RelRN j1 = left.join(JoinRelType.INNER, "on", right);
        RelRN j2 = left.join(JoinRelType.INNER, a, right);
        RelRN j3 = left.join(right);
        RelRN j4 = left.join(a, right);
        return left.filter(e1);
    }

    @Override
    public RelRN after() {
        return left.filter(a);
    }
}