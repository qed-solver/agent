// UNSUPPORTED marker file (kept compilable): see UNSUPPORTED reply.
package org.qed.RRuleInstances;

import org.qed.RelRN;
import org.qed.RRule;

public record ProjectJoinJoinRemove() implements RRule {
    @Override
    public RelRN before() {
        return RelRN.scan("A", "A_Type");
    }

    @Override
    public RelRN after() {
        return RelRN.scan("A", "A_Type");
    }
}
