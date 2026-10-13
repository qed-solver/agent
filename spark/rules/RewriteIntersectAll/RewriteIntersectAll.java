package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// Probe: can QED even represent INTERSECT ALL (bag intersection)?
public record RewriteIntersectAll() implements RRule {
    static final RelRN left = RelRN.scan("Left", "T");
    static final RelRN right = RelRN.scan("Right", "T");

    @Override
    public RelRN before() {
        return left.intersect(true, right);
    }

    @Override
    public RelRN after() {
        return left.intersect(false, right);
    }
}